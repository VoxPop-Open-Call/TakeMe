package pt.famility.backoffice.config.firebase;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Data
@NoArgsConstructor
@ConfigurationProperties
public class FirebaseProperties {

    private Resource configFile;

    private String databaseUrl;

    private String tokenUrl;

    private String appKey;

    private String verifyEmailUrl;

    private String resetPasswordEmailUrl;

}
