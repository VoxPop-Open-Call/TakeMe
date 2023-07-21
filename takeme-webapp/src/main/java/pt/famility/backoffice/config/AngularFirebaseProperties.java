package pt.famility.backoffice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "firebase", ignoreUnknownFields = false)
public class AngularFirebaseProperties {

    private String apiKey;
    private String authDomain;
    private String databaseURL;
    private String projectId;
    private String storageBucket;
    private String messagingSenderId;

}
