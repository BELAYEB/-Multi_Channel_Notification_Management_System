package com.tekup.multi_channel_notification_management_system.infrastructure.sms;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

/**
 * Production-ready SMS service using Twilio
 * Activated when twilio.enabled=true
 */
@Slf4j
@Service
@Primary  // Priorité sur SMSServiceImpl mock
@ConditionalOnProperty(name = "twilio.enabled", havingValue = "true")
public class TwilioSMSServiceImpl implements SMSService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    @PostConstruct
    public void init() {
        log.info("Initializing Twilio SMS Service");
        log.info("Twilio Account SID: {}", accountSid.substring(0, 10) + "...");
        log.info("Twilio Phone Number: {}", fromPhoneNumber);

        // Initialize Twilio
        Twilio.init(accountSid, authToken);

        log.info("✅ Twilio SMS Service initialized successfully");
    }

    @Override
    public void sendSMS(String phoneNumber, String messageText) {
        try {
            log.info("Sending SMS via Twilio from: {} to: {}", fromPhoneNumber, phoneNumber);

            // Validation du numéro
            if (!phoneNumber.startsWith("+")) {
                throw new IllegalArgumentException(
                        "Phone number must be in E.164 format (start with +). Example: +21612345678"
                );
            }

            // Envoyer le SMS
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),      // To
                    new PhoneNumber(fromPhoneNumber),  // From
                    messageText                         // Body
            ).create();

            log.info("✅ SMS sent successfully!");
            log.info("Message SID: {}", message.getSid());
            log.info("Status: {}", message.getStatus());
            log.info("To: {}", phoneNumber);
            log.info("From: {}", fromPhoneNumber);

        } catch (Exception e) {
            log.error("❌ Failed to send SMS to: {}", phoneNumber, e);
            throw new RuntimeException("Twilio SMS sending failed: " + e.getMessage(), e);
        }
    }
}
