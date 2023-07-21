package pt.famility.backoffice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "angular-environment", ignoreUnknownFields = false)
public class AngularEnvironmentProperties {

    private boolean production;
    private String authorizationHeader;
    private String[] whiteListDomains;
    private String[] blackListRoutes;
    private AngularFirebaseProperties firebase;
}
