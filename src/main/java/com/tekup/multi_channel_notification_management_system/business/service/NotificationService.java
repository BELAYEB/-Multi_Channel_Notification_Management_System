package com.tekup.multi_channel_notification_management_system.business.service;


import com.tekup.multi_channel_notification_management_system.business.model.Notification;
import com.tekup.multi_channel_notification_management_system.common.enums.NotificationStatus;
import com.tekup.multi_channel_notification_management_system.common.enums.Priority;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface following Interface Segregation Principle
 */
public interface NotificationService {

    Notification sendNotification(Notification notification);

    Notification getNotificationById(Long id);

    List<Notification> getAllNotifications();

    List<Notification> getNotificationsByStatus(NotificationStatus status);

    List<Notification> getNotificationsByPriority(Priority priority);

    List<Notification> getNotificationsByRecipient(String recipient);

    List<Notification> getNotificationsByDateRange(LocalDateTime start, LocalDateTime end);

    void retryFailedNotifications();

    long getNotificationCount();
}
