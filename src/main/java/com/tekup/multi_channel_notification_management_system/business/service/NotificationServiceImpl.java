package com.tekup.multi_channel_notification_management_system.business.service;



import com.tekup.multi_channel_notification_management_system.business.handler.NotificationChainBuilder;
import com.tekup.multi_channel_notification_management_system.business.handler.NotificationHandler;
import com.tekup.multi_channel_notification_management_system.business.model.Notification;
import com.tekup.multi_channel_notification_management_system.business.pattern.MessageTemplate;
import com.tekup.multi_channel_notification_management_system.business.pattern.MessageTemplateFactory;
import com.tekup.multi_channel_notification_management_system.common.enums.NotificationStatus;
import com.tekup.multi_channel_notification_management_system.common.enums.Priority;
import com.tekup.multi_channel_notification_management_system.common.exception.NotificationException;
import com.tekup.multi_channel_notification_management_system.common.exception.ResourceNotFoundException;
import com.tekup.multi_channel_notification_management_system.common.util.Constants;
import com.tekup.multi_channel_notification_management_system.persistence.entity.NotificationEntity;
import com.tekup.multi_channel_notification_management_system.persistence.mapper.NotificationMapper;
import com.tekup.multi_channel_notification_management_system.persistence.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation following Single Responsibility Principle
 * and Dependency Inversion Principle
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final MessageTemplateFactory templateFactory;
    private final NotificationChainBuilder chainBuilder;

    @Override
    @Transactional
    public Notification sendNotification(Notification notification) {
        log.info("Processing notification for recipient: {}", notification.getRecipient());

        try {
            // Apply template if specified
            if (notification.getTemplateType() != null) {
                applyTemplate(notification);
            }

            // Save notification with PENDING status
            NotificationEntity entity = mapper.toEntity(notification);
            entity = repository.save(entity);

            // Get the handler chain and process
            NotificationHandler chain = chainBuilder.buildChain();
            Notification savedNotification = mapper.toModel(entity);

            try {
                chain.handle(savedNotification);

                // Mark as sent
                entity.markAsSent();
                entity = repository.save(entity);

                log.info("Notification sent successfully. ID: {}", entity.getId());

            } catch (Exception e) {
                log.error("Failed to send notification", e);
                entity.markAsFailed(e.getMessage());
                repository.save(entity);
                throw new NotificationException("Failed to send notification: " + e.getMessage());
            }

            return mapper.toModel(entity);

        } catch (Exception e) {
            log.error("Error processing notification", e);
            throw new NotificationException("Error processing notification", e);
        }
    }

    private void applyTemplate(Notification notification) {
        MessageTemplate template = templateFactory.getTemplate(notification.getTemplateType());

        String subject = template.renderTitle(notification.getCustomData());
        String message = template.renderBody(notification.getCustomData());

        notification.setSubject(subject);
        notification.setMessage(message);

        log.debug("Template applied: {}", notification.getTemplateType());
    }

    @Override
    @Transactional(readOnly = true)
    public Notification getNotificationById(Long id) {
        log.debug("Fetching notification with ID: {}", id);

        NotificationEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", id));

        return mapper.toModel(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getAllNotifications() {
        log.debug("Fetching all notifications");

        return repository.findAll().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByStatus(NotificationStatus status) {
        log.debug("Fetching notifications with status: {}", status);

        return repository.findByStatus(status).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByPriority(Priority priority) {
        log.debug("Fetching notifications with priority: {}", priority);

        return repository.findByPriority(priority).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByRecipient(String recipient) {
        log.debug("Fetching notifications for recipient: {}", recipient);

        return repository.findByRecipient(recipient).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByDateRange(LocalDateTime start, LocalDateTime end) {
        log.debug("Fetching notifications between {} and {}", start, end);

        return repository.findByDateRange(start, end).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void retryFailedNotifications() {
        log.info("Retrying failed notifications");

        List<NotificationEntity> failedNotifications =
                repository.findFailedNotificationsForRetry(Constants.MAX_RETRY_ATTEMPTS);

        log.info("Found {} notifications to retry", failedNotifications.size());

        NotificationHandler chain = chainBuilder.buildChain();

        for (NotificationEntity entity : failedNotifications) {
            try {
                entity.incrementRetryCount();
                entity.setStatus(NotificationStatus.RETRYING);
                repository.save(entity);

                Notification notification = mapper.toModel(entity);
                chain.handle(notification);

                entity.markAsSent();
                repository.save(entity);

                log.info("Notification {} retried successfully", entity.getId());

            } catch (Exception e) {
                log.error("Retry failed for notification {}", entity.getId(), e);
                entity.markAsFailed("Retry failed: " + e.getMessage());
                repository.save(entity);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getNotificationCount() {
        return repository.count();
    }
}
