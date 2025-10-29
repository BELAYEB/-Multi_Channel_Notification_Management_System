package com.tekup.multi_channel_notification_management_system.business.handler;

import com.tekup.multi_channel_notification_management_system.business.model.Notification;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for Notification Handlers implementing common logic
 */


@Slf4j
public abstract class BaseNotificationHandler implements NotificationHandler {

    protected NotificationHandler next;
    protected final String handlerName;

    protected BaseNotificationHandler(String handlerName) {
        this.handlerName = handlerName;
    }

    @Override
    public void setNext(NotificationHandler handler) {
        this.next = handler;
        log.info("🔗 {} → chaîné avec {}",
                this.handlerName,
                ((BaseNotificationHandler)handler).handlerName);
    }

    protected void passToNext(Notification notification) {
        if (next != null) {
            log.info("⏭️  {} ne peut pas traiter. Passage à {} →",
                    handlerName,
                    ((BaseNotificationHandler)next).handlerName);
            next.handle(notification);
        } else {
            log.warn("⛔ {} : Fin de chaîne atteinte. Aucun handler ne peut traiter le canal: {}",
                    handlerName,
                    notification.getChannel());
        }
    }

    protected abstract boolean canHandle(Notification notification);
    protected abstract void process(Notification notification);
}
