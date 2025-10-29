package com.tekup.multi_channel_notification_management_system.business.handler;



import com.tekup.multi_channel_notification_management_system.business.model.Notification;
import com.tekup.multi_channel_notification_management_system.common.enums.Channel;
import com.tekup.multi_channel_notification_management_system.infrastructure.push.PushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Concrete handler for Push notifications
 */
@Slf4j
@Component
public class PushHandler extends BaseNotificationHandler {

    private final PushService pushService;


    public PushHandler(PushService pushService) {
        super("🔔 PushHandler");
        this.pushService = pushService;
    }

    @Override
    public void handle(Notification notification) {
        if (canHandle(notification)) {
            log.info("PushHandler processing notification for: {}",
                    notification.getRecipient());
            process(notification);
        } else {
            passToNext(notification);
        }
    }

    @Override
    protected boolean canHandle(Notification notification) {
        return notification.getChannel() == Channel.PUSH;
    }

    @Override
    protected void process(Notification notification) {
        try {
            pushService.sendPushNotification(
                    notification.getRecipient(),
                    notification.getSubject(),
                    notification.getMessage()
            );
            log.info("Push notification sent successfully to: {}",
                    notification.getRecipient());
        } catch (Exception e) {
            log.error("Failed to send push notification to: {}",
                    notification.getRecipient(), e);
            throw new RuntimeException("Push notification sending failed", e);
        }
    }
}
