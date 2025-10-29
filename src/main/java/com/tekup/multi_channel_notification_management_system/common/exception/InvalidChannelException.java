package com.tekup.multi_channel_notification_management_system.common.exception;


public class InvalidChannelException extends NotificationException {

    public InvalidChannelException(String channel) {
        super(String.format("Invalid notification channel: %s", channel),
                "INVALID_CHANNEL");
    }
}
