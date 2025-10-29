package com.tekup.multi_channel_notification_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Main Spring Boot Application
 * Architecture: Layered Architecture (Presentation, Business, Persistence, Infrastructure)
 * Design Patterns: Chain of Responsibility, Flyweight
 * Principles: SOLID
 */


@SpringBootApplication
public class MultiChannelNotificationManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiChannelNotificationManagementSystemApplication.class, args);
    }

}
