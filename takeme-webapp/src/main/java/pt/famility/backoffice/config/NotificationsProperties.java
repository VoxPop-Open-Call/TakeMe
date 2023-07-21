package pt.famility.backoffice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "notifications", ignoreUnknownFields = false)
public class NotificationsProperties {
    private boolean childSubscriptionServiceToggle;
}
