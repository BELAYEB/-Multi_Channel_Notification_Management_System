package com.tekup.multi_channel_notification_management_system.common.enums;

public enum Channel {
    EMAIL("Email"),
    SMS("SMS"),
    PUSH("Push Notification");

    private final String displayName;

    Channel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
