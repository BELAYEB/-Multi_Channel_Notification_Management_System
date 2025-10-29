package com.tekup.multi_channel_notification_management_system.business.handler;

import com.tekup.multi_channel_notification_management_system.business.model.Notification;
import com.tekup.multi_channel_notification_management_system.common.enums.Channel;
import com.tekup.multi_channel_notification_management_system.infrastructure.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Concrete handler for Email notifications
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailHandler extends BaseNotificationHandler {

    private final EmailService emailService;

    @Override
    public void handle(Notification notification) {
        if (canHandle(notification)) {
            log.info("EmailHandler processing notification for: {}",
                    notification.getRecipient());
            process(notification);
        } else {
            passToNext(notification);
        }
    }

    @Override
    protected boolean canHandle(Notification notification) {
        return notification.getChannel() == Channel.EMAIL;
    }

    @Override
    protected void process(Notification notification) {
        try {
            emailService.sendEmail(
                    notification.getRecipient(),
                    notification.getSubject(),
                    notification.getMessage()
            );
            log.info("Email sent successfully to: {}", notification.getRecipient());
        } catch (Exception e) {
            log.error("Failed to send email to: {}", notification.getRecipient(), e);
            throw new RuntimeException("Email sending failed", e);
        }
    }
}
