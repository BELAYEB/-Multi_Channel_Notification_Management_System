package com.tekup.multi_channel_notification_management_system.persistence.mapper;


import com.tekup.multi_channel_notification_management_system.business.model.Notification;
import com.tekup.multi_channel_notification_management_system.persistence.entity.NotificationEntity;
import com.tekup.multi_channel_notification_management_system.presentation.dto.NotificationRequest;
import com.tekup.multi_channel_notification_management_system.presentation.dto.NotificationResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    public NotificationEntity toEntity(Notification notification) {
        if (notification == null) {
            return null;
        }

        return NotificationEntity.builder()
                .id(notification.getId())
                .recipient(notification.getRecipient())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .channel(notification.getChannel())
                .priority(notification.getPriority())
                .status(notification.getStatus())
                .templateType(notification.getTemplateType())
                .customData(notification.getCustomData())
                .retryCount(notification.getRetryCount())
                .errorMessage(notification.getErrorMessage())
                .sentAt(notification.getSentAt())
                .build();
    }

    public Notification toModel(NotificationEntity entity) {
        if (entity == null) {
            return null;
        }

        return Notification.builder()
                .id(entity.getId())
                .recipient(entity.getRecipient())
                .subject(entity.getSubject())
                .message(entity.getMessage())
                .channel(entity.getChannel())
                .priority(entity.getPriority())
                .status(entity.getStatus())
                .templateType(entity.getTemplateType())
                .customData(entity.getCustomData())
                .retryCount(entity.getRetryCount())
                .errorMessage(entity.getErrorMessage())
                .sentAt(entity.getSentAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public NotificationResponse toResponse(NotificationEntity entity) {
        if (entity == null) {
            return null;
        }

        return NotificationResponse.builder()
                .id(entity.getId())
                .recipient(entity.getRecipient())
                .subject(entity.getSubject())
                .channel(entity.getChannel().name())
                .priority(entity.getPriority().name())
                .status(entity.getStatus().name())
                .sentAt(entity.getSentAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public Notification fromRequest(NotificationRequest request) {
        if (request == null) {
            return null;
        }

        return Notification.builder()
                .recipient(request.getRecipient())
                .subject(request.getSubject())
                .message(request.getMessage())
                .channel(request.getChannel())
                .priority(request.getPriority())
                .templateType(request.getTemplateType())
                .customData(request.getCustomData())
                .build();
    }

    public List<NotificationResponse> toResponseList(List<NotificationEntity> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
