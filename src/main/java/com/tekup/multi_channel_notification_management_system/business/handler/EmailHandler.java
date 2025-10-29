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
public class EmailHandler extends BaseNotificationHandler {

    private final EmailService emailService;

    public EmailHandler(EmailService emailService) {
        super("📧 EmailHandler");
        this.emailService = emailService;
    }

    @Override
    public void handle(Notification notification) {
        log.info("════════════════════════════════════════");
        log.info("🔍 {} reçoit la notification", handlerName);
        log.info("   Canal demandé: {}", notification.getChannel());
        log.info("   Destinataire: {}", notification.getRecipient());

        if (canHandle(notification)) {
            log.info("✅ {} PEUT traiter cette notification!", handlerName);
            log.info("🚀 Traitement en cours...");
            process(notification);
            log.info("✅ {} a TERMINÉ avec succès!", handlerName);
        } else {
            log.info("❌ {} ne peut PAS traiter le canal {}", handlerName, notification.getChannel());
            passToNext(notification);
        }
        log.info("════════════════════════════════════════");
    }

    @Override
    protected boolean canHandle(Notification notification) {
        return notification.getChannel() == Channel.EMAIL;
    }

    @Override
    protected void process(Notification notification) {
        emailService.sendEmail(
                notification.getRecipient(),
                notification.getSubject(),
                notification.getMessage()
        );
    }
}
