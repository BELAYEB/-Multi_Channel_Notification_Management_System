package com.tekup.multi_channel_notification_management_system.business.model;


import com.tekup.multi_channel_notification_management_system.common.enums.Channel;
import com.tekup.multi_channel_notification_management_system.common.enums.NotificationStatus;
import com.tekup.multi_channel_notification_management_system.common.enums.Priority;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private Long id;
    private String recipient;
    private String subject;
    private String message;
    private Channel channel;
    private Priority priority;

    @Builder.Default
    private NotificationStatus status = NotificationStatus.PENDING;

    private String templateType;

    @Builder.Default
    private Map<String, String> customData = new HashMap<>();

    @Builder.Default
    private int retryCount = 0;

    private String errorMessage;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;

    public boolean canRetry(int maxRetries) {
        return this.status == NotificationStatus.FAILED
                && this.retryCount < maxRetries;
    }

    public void incrementRetry() {
        this.retryCount++;
        this.status = NotificationStatus.RETRYING;
    }
}
