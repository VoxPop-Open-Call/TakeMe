package pt.famility.backoffice.service.dto.geolocation;


import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.util.List;

@Data
@Builder
public class ItineraryDirections {

    private Duration totalDuration;
    private Duration totalDurationInTraffic;
    private Distance totalDistance;
    private Duration stopTimeByStopPoint;

    private List<DirectionLeg> itineraryLegs;
}
