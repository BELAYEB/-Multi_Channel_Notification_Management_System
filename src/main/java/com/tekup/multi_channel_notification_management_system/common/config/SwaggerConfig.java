package com.tekup.multi_channel_notification_management_system.common.config;



import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration for API documentation
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI notificationSystemOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Notification System API")
                        .description("Multi-channel notification system with Chain of Responsibility and Flyweight patterns")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your.email@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
