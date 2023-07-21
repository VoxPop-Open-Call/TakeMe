package pt.famility.backoffice.service.dto.geolocation;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a distance. Inspired by java.lang.time.Duration.
 */
@Data
@AllArgsConstructor
public class Distance {
    private long distanceInMeters;

    public static Distance ofMeters(long distanceInMeters) {
        return new Distance(distanceInMeters);
    }
}
