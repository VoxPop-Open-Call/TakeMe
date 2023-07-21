package pt.famility.backoffice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "photo", ignoreUnknownFields = false)
public class PhotoProperties {

    private Integer width;
    private Integer height;
    private String contentType;
}
