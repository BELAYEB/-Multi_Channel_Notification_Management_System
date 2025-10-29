package com.tekup.multi_channel_notification_management_system.common.util;

public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String API_BASE_PATH = "/api/v1";
    public static final String NOTIFICATIONS_PATH = API_BASE_PATH + "/notifications";

    public static final int MAX_RETRY_ATTEMPTS = 3;
    public static final int RETRY_DELAY_MS = 5000;

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
