package com.tekup.multi_channel_notification_management_system.infrastructure.push;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Service("firebasePushService")
@Primary
public class FirebasePushServiceImpl implements PushService {

    @Value("${firebase.enabled:false}")
    private boolean firebaseEnabled;

    @Value("${firebase.credentials.path}")
    private Resource credentialsResource;

    private boolean isInitialized = false;

    @PostConstruct
    public void init() {
        if (!firebaseEnabled) {
            log.warn("⚠️ Firebase is DISABLED");
            return;
        }

        try {
            log.info("========================================");
            log.info("🚀 Initializing Firebase Push Service");
            log.info("========================================");

            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(credentialsResource.getInputStream());

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            isInitialized = true;
            log.info("✅ Firebase initialized SUCCESSFULLY");
            log.info("========================================");

        } catch (IOException e) {
            log.error("❌ Failed to initialize Firebase", e);
            isInitialized = false;
        }
    }

    @Override
    public void sendPushNotification(String deviceToken, String title, String body) {
        if (!isInitialized) {
            log.error("❌ Firebase not initialized");
            throw new RuntimeException("Firebase not initialized");
        }

        try {
            log.info("========================================");
            log.info("📱 Sending Push via FIREBASE");
            log.info("Token: {}...", deviceToken.substring(0, Math.min(30, deviceToken.length())));
            log.info("Title: {}", title);
            log.info("Body: {}", body);

            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message message = Message.builder()
                    .setToken(deviceToken)
                    .setNotification(notification)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);

            log.info("✅ Push SENT SUCCESSFULLY!");
            log.info("Response: {}", response);
            log.info("========================================");

        } catch (Exception e) {
            log.error("❌ FAILED to send push", e);
            throw new RuntimeException("Push failed: " + e.getMessage(), e);
        }
    }
}
