package com.tekup.multi_channel_notification_management_system.infrastructure.push;


/**
 * Push notification service interface following Dependency Inversion Principle
 */
public interface PushService {
    void sendPushNotification(String deviceToken, String title, String body);
}
