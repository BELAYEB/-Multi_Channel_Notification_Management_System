package com.tekup.multi_channel_notification_management_system.business.handler;



import com.tekup.multi_channel_notification_management_system.business.model.Notification;

/**
 * Chain of Responsibility Pattern - Handler Interface
 */
public interface NotificationHandler {
    void setNext(NotificationHandler handler);
    void handle(Notification notification);
}
