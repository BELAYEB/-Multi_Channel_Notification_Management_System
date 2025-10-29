package com.tekup.multi_channel_notification_management_system.business.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Builder for creating the notification handler chain
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationChainBuilder {

    private final EmailHandler emailHandler;
    private final SMSHandler smsHandler;
    private final PushHandler pushHandler;

    public NotificationHandler buildChain() {
        log.info("╔════════════════════════════════════════╗");
        log.info("║   CONSTRUCTION DE LA CHAÎNE            ║");
        log.info("╚════════════════════════════════════════╝");

        // Construire la chaîne: Email → SMS → Push
        emailHandler.setNext(smsHandler);
        smsHandler.setNext(pushHandler);

        log.info("✅ Chaîne construite: 📧 Email → 📱 SMS → 🔔 Push");
        log.info("════════════════════════════════════════");

        return emailHandler; // Retourner le premier maillon
    }
}
