package pt.famility.backoffice.config;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "itinerary", ignoreUnknownFields = false)
public class ItineraryProperties {
    private Integer waypointTime;
}
