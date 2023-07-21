package pt.famility.backoffice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import pt.famility.backoffice.config.firebase.FirebaseProperties;

/**
 * Properties specific to Famility Backoffice.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private FirebaseProperties firebase;
    private PhotoProperties photo;
    private GoogleMapsProperties googleMaps;
    private AngularEnvironmentProperties angularEnvironment;
    private Integer itineraryStopPointsByChild;
    private NotificationsProperties notifications;
    private RegistrationProperties registration;
    private ItineraryProperties itinerary;

    @Data
    @NoArgsConstructor
    public static class RegistrationProperties {
        private String termsAndConditionsURL;
    }

}
