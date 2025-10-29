package com.tekup.multi_channel_notification_management_system.infrastructure.email;


/**
 * Email service interface following Dependency Inversion Principle
 */
public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendHtmlEmail(String to, String subject, String htmlBody);
}
