package com.tekup.multi_channel_notification_management_system.presentation.controller;


import com.tekup.multi_channel_notification_management_system.business.model.Notification;
import com.tekup.multi_channel_notification_management_system.business.pattern.MessageTemplateFactory;
import com.tekup.multi_channel_notification_management_system.business.service.NotificationService;
import com.tekup.multi_channel_notification_management_system.common.enums.NotificationStatus;
import com.tekup.multi_channel_notification_management_system.common.enums.Priority;
import com.tekup.multi_channel_notification_management_system.common.util.Constants;
import com.tekup.multi_channel_notification_management_system.persistence.entity.NotificationEntity;
import com.tekup.multi_channel_notification_management_system.persistence.mapper.NotificationMapper;
import com.tekup.multi_channel_notification_management_system.persistence.repository.NotificationRepository;
import com.tekup.multi_channel_notification_management_system.presentation.dto.NotificationRequest;
import com.tekup.multi_channel_notification_management_system.presentation.dto.NotificationResponse;
import com.tekup.multi_channel_notification_management_system.presentation.dto.NotificationStatsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller following Single Responsibility Principle
 */
@Slf4j
@RestController
@RequestMapping(Constants.NOTIFICATIONS_PATH)
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper mapper;
    private final NotificationRepository repository;
    private final MessageTemplateFactory templateFactory;

    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(
            @Valid @RequestBody NotificationRequest request) {
        log.info("Received request to send notification to: {}", request.getRecipient());

        Notification notification = mapper.fromRequest(request);
        Notification sent = notificationService.sendNotification(notification);
        NotificationEntity entity = mapper.toEntity(sent);

        NotificationResponse response = mapper.toResponse(entity);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotification(@PathVariable Long id) {
        log.info("Fetching notification with ID: {}", id);

        Notification notification = notificationService.getNotificationById(id);
        NotificationEntity entity = mapper.toEntity(notification);
        NotificationResponse response = mapper.toResponse(entity);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String recipient) {

        log.info("Fetching notifications with filters - status: {}, priority: {}, recipient: {}",
                status, priority, recipient);

        List<Notification> notifications;

        if (status != null) {
            notifications = notificationService.getNotificationsByStatus(
                    NotificationStatus.valueOf(status.toUpperCase()));
        } else if (priority != null) {
            notifications = notificationService.getNotificationsByPriority(
                    Priority.valueOf(priority.toUpperCase()));
        } else if (recipient != null) {
            notifications = notificationService.getNotificationsByRecipient(recipient);
        } else {
            notifications = notificationService.getAllNotifications();
        }

        List<NotificationEntity> entities = notifications.stream()
                .map(mapper::toEntity)
                .toList();

        List<NotificationResponse> responses = mapper.toResponseList(entities);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        log.info("Fetching notifications between {} and {}", start, end);

        List<Notification> notifications = notificationService.getNotificationsByDateRange(start, end);
        List<NotificationEntity> entities = notifications.stream()
                .map(mapper::toEntity)
                .toList();

        List<NotificationResponse> responses = mapper.toResponseList(entities);

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/retry-failed")
    public ResponseEntity<String> retryFailedNotifications() {
        log.info("Triggering retry for failed notifications");

        notificationService.retryFailedNotifications();

        return ResponseEntity.ok("Failed notifications retry triggered successfully");
    }

    @GetMapping("/stats")
    public ResponseEntity<NotificationStatsResponse> getNotificationStats() {
        log.info("Fetching notification statistics");

        NotificationStatsResponse stats = NotificationStatsResponse.builder()
                .totalNotifications(repository.count())
                .sentNotifications(repository.countByStatus(NotificationStatus.SENT))
                .failedNotifications(repository.countByStatus(NotificationStatus.FAILED))
                .pendingNotifications(repository.countByStatus(NotificationStatus.PENDING))
                .cachedTemplates(templateFactory.getTemplateCount())
                .build();

        return ResponseEntity.ok(stats);
    }
}
