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
        log.info("Building notification handler chain");

        // Build the chain: Email -> SMS -> Push
        emailHandler.setNext(smsHandler);
        smsHandler.setNext(pushHandler);

        return emailHandler; // Return the first handler
    }
}
