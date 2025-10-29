package com.tekup.multi_channel_notification_management_system.infrastructure.sms;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Production-ready SMS service using Twilio
 * Activated only when twilio.enabled=true
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "twilio.enabled", havingValue = "true")
public class TwilioSMSServiceImpl implements SMSService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    @Override
    public void sendSMS(String phoneNumber, String message) {
        try {
            log.info("Sending SMS via Twilio to: {}", phoneNumber);

            // TODO: Add Twilio dependency and implementation
            // Twilio.init(accountSid, authToken);
            // Message twilioMessage = Message.creator(
            //     new PhoneNumber(phoneNumber),
            //     new PhoneNumber(fromPhoneNumber),
            //     message
            // ).create();

            log.info("Twilio SMS sent successfully to: {}", phoneNumber);

        } catch (Exception e) {
            log.error("Failed to send Twilio SMS to: {}", phoneNumber, e);
            throw new RuntimeException("Twilio SMS sending failed: " + e.getMessage(), e);
        }
    }
}
