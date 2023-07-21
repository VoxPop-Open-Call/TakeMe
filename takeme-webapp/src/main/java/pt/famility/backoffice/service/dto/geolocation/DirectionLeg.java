package pt.famility.backoffice.service.dto.geolocation;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

/**
 * Application model to hold information about directions between 2 points.
 *
 */
@Data
@Builder
public class DirectionLeg {

    private Duration duration;
    private Duration durationInTraffic;
    private Distance distance;

}
