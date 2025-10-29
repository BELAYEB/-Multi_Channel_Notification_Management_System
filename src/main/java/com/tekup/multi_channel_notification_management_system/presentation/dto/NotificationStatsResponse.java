package com.tekup.multi_channel_notification_management_system.presentation.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationStatsResponse {
    private long totalNotifications;
    private long sentNotifications;
    private long failedNotifications;
    private long pendingNotifications;
    private int cachedTemplates;
}
