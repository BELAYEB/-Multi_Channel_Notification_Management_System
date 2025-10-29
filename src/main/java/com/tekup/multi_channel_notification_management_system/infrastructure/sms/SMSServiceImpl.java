package com.tekup.multi_channel_notification_management_system.infrastructure.sms;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * SMS service implementation (Mock implementation for demo)
 * In production, integrate with Twilio, AWS SNS, or other SMS providers
 */
@Slf4j
@Service
public class SMSServiceImpl implements SMSService {

    @Override
    public void sendSMS(String phoneNumber, String message) {
        try {
            log.info("Sending SMS to: {}", phoneNumber);

            // TODO: Integrate with actual SMS provider (Twilio, AWS SNS, etc.)
            // Example with Twilio:
            // Message twilioMessage = Message.creator(
            //     new PhoneNumber(phoneNumber),
            //     new PhoneNumber(fromNumber),
            //     message
            // ).create();

            // Mock implementation for demo
            simulateSMSSending(phoneNumber, message);

            log.info("SMS sent successfully to: {}", phoneNumber);

        } catch (Exception e) {
            log.error("Failed to send SMS to: {}", phoneNumber, e);
            throw new RuntimeException("SMS sending failed: " + e.getMessage(), e);
        }
    }

    private void simulateSMSSending(String phoneNumber, String message) {
        // Simulate SMS sending delay
        try {
            Thread.sleep(500);
            log.debug("SMS simulated for: {} with message length: {}",
                    phoneNumber, message.length());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("SMS simulation interrupted", e);
        }
    }
}
