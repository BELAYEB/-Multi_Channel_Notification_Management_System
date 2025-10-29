package com.tekup.multi_channel_notification_management_system.business.handler;



import com.tekup.multi_channel_notification_management_system.business.model.Notification;
import com.tekup.multi_channel_notification_management_system.common.enums.Channel;
import com.tekup.multi_channel_notification_management_system.infrastructure.sms.SMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Concrete handler for SMS notifications
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SMSHandler extends BaseNotificationHandler {

    private final SMSService smsService;

    @Override
    public void handle(Notification notification) {
        if (canHandle(notification)) {
            log.info("SMSHandler processing notification for: {}",
                    notification.getRecipient());
            process(notification);
        } else {
            passToNext(notification);
        }
    }

    @Override
    protected boolean canHandle(Notification notification) {
        return notification.getChannel() == Channel.SMS;
    }

    @Override
    protected void process(Notification notification) {
        try {
            smsService.sendSMS(
                    notification.getRecipient(),
                    notification.getMessage()
            );
            log.info("SMS sent successfully to: {}", notification.getRecipient());
        } catch (Exception e) {
            log.error("Failed to send SMS to: {}", notification.getRecipient(), e);
            throw new RuntimeException("SMS sending failed", e);
        }
    }
}
