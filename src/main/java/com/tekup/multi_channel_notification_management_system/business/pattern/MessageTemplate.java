package com.tekup.multi_channel_notification_management_system.business.pattern;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Flyweight Pattern - Shared Message Template
 * Intrinsic state: templateId, titlePattern, bodyPattern, format
 */
@Slf4j
@Getter
@AllArgsConstructor
public class MessageTemplate {

    // Intrinsic state (shared)
    private final String templateId;
    private final String titlePattern;
    private final String bodyPattern;
    private final String format;

    /**
     * Render template with extrinsic state (context-specific data)
     */
    public String renderTitle(Map<String, String> data) {
        log.debug("Rendering title for template: {}", templateId);
        return replacePlaceholders(titlePattern, data);
    }

    public String renderBody(Map<String, String> data) {
        log.debug("Rendering body for template: {}", templateId);
        return replacePlaceholders(bodyPattern, data);
    }

    private String replacePlaceholders(String pattern, Map<String, String> data) {
        if (pattern == null || data == null) {
            return pattern;
        }

        String result = pattern;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            result = result.replace(placeholder, entry.getValue());
        }

        return result;
    }

    public String formatMessage(String title, String body) {
        return switch (format.toUpperCase()) {
            case "HTML" -> String.format("<h1>%s</h1><p>%s</p>", title, body);
            case "MARKDOWN" -> String.format("# %s\n\n%s", title, body);
            default -> String.format("%s\n\n%s", title, body);
        };
    }
}
