package com.tekup.multi_channel_notification_management_system.infrastructure.sms;

/**
 * SMS service interface following Dependency Inversion Principle
 */
public interface SMSService {
    void sendSMS(String phoneNumber, String message);
}
