package pt.famility.backoffice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "google-maps", ignoreUnknownFields = false)
public class GoogleMapsProperties {

    private String apiKey;
    private boolean withTraffic;
}
