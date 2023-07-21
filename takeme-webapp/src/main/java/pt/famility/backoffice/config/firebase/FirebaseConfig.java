package pt.famility.backoffice.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import pt.famility.backoffice.config.ApplicationProperties;


import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    private final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public DatabaseReference firebaseDatabase() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @PostConstruct
    public void init() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseProperties firebaseProperties = applicationProperties.getFirebase();

            log.debug("Initializing the Firebase's Configuration");
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(firebaseProperties.getConfigFile().getInputStream()))
                .setDatabaseUrl(firebaseProperties.getDatabaseUrl())
                .build();
            FirebaseApp.initializeApp(options);
        } else {
            log.info("FirebaseApp is already initialized.");
        }
    }


}
