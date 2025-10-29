package com.tekup.multi_channel_notification_management_system.business.handler;



import com.tekup.multi_channel_notification_management_system.business.model.Notification;
import lombok.extern.slf4j.Slf4j;

/**
 * Base handler implementing common chain logic
 */
@Slf4j
public abstract class BaseNotificationHandler implements NotificationHandler {

    protected NotificationHandler next;

    @Override
    public void setNext(NotificationHandler handler) {
        this.next = handler;
    }

    protected void passToNext(Notification notification) {
        if (next != null) {
            log.debug("Passing notification to next handler in chain");
            next.handle(notification);
        } else {
            log.warn("End of chain reached. No handler found for notification: {}",
                    notification.getChannel());
        }
    }

    protected abstract boolean canHandle(Notification notification);
    protected abstract void process(Notification notification);
}
