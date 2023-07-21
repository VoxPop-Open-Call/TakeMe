package pt.famility.backoffice.config;

import com.google.maps.GeoApiContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GoogleMapsConfiguration {

    private final Logger log = LoggerFactory.getLogger(GoogleMapsConfiguration.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public GeoApiContext geoApiContext() {
        GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(applicationProperties.getGoogleMaps().getApiKey())
            .build();

        log.info("Google Maps client initialized.");

        return context;

    }


}
