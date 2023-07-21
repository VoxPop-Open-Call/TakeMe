package pt.famility.backoffice.service;

import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pt.famility.backoffice.config.ApplicationProperties;
import pt.famility.backoffice.service.dto.geolocation.DirectionLeg;
import pt.famility.backoffice.service.dto.geolocation.Distance;
import pt.famility.backoffice.service.dto.geolocation.ItineraryDirections;
import pt.famility.backoffice.service.exception.InvalidItineraryException;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This service is not a strict facade to Google Maps service.
 * It does also some calculations on top of that.
 *
 */
@Slf4j
@Service
public class GoogleMapsService {

    private final GeoApiContext geoApiContext;
    private final ApplicationProperties applicationProperties;
    private final GoogleMapsDelegate googleMapsDelegate;

    public GoogleMapsService(ApplicationProperties applicationProperties,
                             GeoApiContext geoApiContext,
                             GoogleMapsDelegate googleMapsDelegate) {
        this.applicationProperties = applicationProperties;
        this.geoApiContext = geoApiContext;
        this.googleMapsDelegate = googleMapsDelegate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void testConnection() throws InterruptedException, ApiException, IOException {
        LatLng source = new LatLng(38.7385305,-9.1517655);
        LatLng target = new LatLng(38.726115, -9.150161);

        this.distanceTime(source, target);

        log.info("Google Maps client connectivity is OK.");
    }

    public long distanceTime(LatLng origin, LatLng destination) throws InterruptedException, ApiException, IOException {
        long time = 0;

        boolean withTraffic = this.applicationProperties.getGoogleMaps().isWithTraffic();

        log.debug("Calling Google Maps API to calculate the ETA between {} and {}. Traffic enabled? {}", origin, destination, withTraffic);

        DistanceMatrix results = googleMapsDelegate
            .newDistanceMatrixRequest(geoApiContext)
            .mode(withTraffic ? TravelMode.TRANSIT : TravelMode.DRIVING)
            .origins( new LatLng[] { origin })
            .destinations(new LatLng[] { destination })
            .departureTime(Instant.now())
            .await();

        // API didn't throw an exception. Assuming that there is a valid response
        DistanceMatrixElement result = results.rows[0].elements[0];

        if (DistanceMatrixElementStatus.OK.equals(result.status)) {
            if (result.durationInTraffic != null) {
                time = result.durationInTraffic.inSeconds;
            } else {
                time = result.duration.inSeconds;
            }
            log.debug("ETA between {} and {}: {} seconds", origin, destination, time);
        } else {
            log.info("Google Maps API didn't return a valid result for ETA between {} and {}. Returned status: {}. Returning {}.", origin, destination, result.status, time);
        }

        return time;
    }

    /**
     * Tries to obtain some ItineraryDirections for the given route. If fails to do so, returns none
     * @param departureTime
     * @param stopTimeByWaypoint
     * @param origin
     * @param destination
     * @param waypoints
     * @return some ItineraryDirections for given route, or none if itinerary not possible to compute
     */
    Optional<ItineraryDirections> calculateItineraryETA(Instant departureTime,
                                                        Duration stopTimeByWaypoint,
                                                        LatLng origin,
                                                        LatLng destination,
                                                        LatLng... waypoints) {
        List<DirectionLeg> legs = null;

        try {
            legs = calculateETA(departureTime, origin, destination, waypoints);
        } catch (Exception ex) {
            // not validating itineraries
            log.warn("Exception {} calculating ETA of from {} to {} starting at {}", ex, origin, destination, departureTime);
        }

        if (legs == null || legs.isEmpty()) {
            return Optional.empty();
        }

        legs.stream()
                .skip(1)
                .forEach(leg -> {
                    Duration duration = leg.getDuration();
                    Duration durationWithStopTime = duration.plus(stopTimeByWaypoint);
                    leg.setDuration(durationWithStopTime);
                });

        ItineraryDirections itineraryDirections = ItineraryDirections.builder()
            .stopTimeByStopPoint(stopTimeByWaypoint)
            .itineraryLegs(legs)
            .totalDistance(legs.stream()
                .map(DirectionLeg::getDistance)
                .reduce(Distance.ofMeters(0),
                    (d1, d2) -> Distance.ofMeters(d1.getDistanceInMeters() + d2.getDistanceInMeters())
                )
            )
            .totalDuration(legs.stream()
                .map(DirectionLeg::getDuration)
                .reduce(Duration.ofSeconds(0),
                        Duration::plus
                )
            )
            .totalDurationInTraffic(legs.stream()
                .map(DirectionLeg::getDurationInTraffic)
                .reduce(Duration.ofSeconds(0),
                    (d1, d2) -> {
                        if (d1 != null && d2 != null) {
                            return d1.plus(d2);
                        } else {
                            return null;
                        }
                    }
                )
            )
            .build();

        return Optional.of(itineraryDirections);
    }

    protected List<DirectionLeg> calculateETA(Instant departureTime,
                                            LatLng origin,
                                            LatLng destination,
                                            LatLng... waypoints) throws InterruptedException, ApiException, IOException {
        long time = 0;

        boolean withTraffic = this.applicationProperties.getGoogleMaps().isWithTraffic();

        log.debug("Calling Google Maps API to calculate the ETA between {} and {} thru {}. Traffic enabled? {}", origin, destination, waypoints, withTraffic);

        DirectionsApiRequest req =
            googleMapsDelegate
                .newDirectionsRequest(geoApiContext)
                .mode(withTraffic ? TravelMode.TRANSIT : TravelMode.DRIVING)
                .origin(origin)
                .destination(destination)
                .waypoints(waypoints)
                // optimize means that the waypoints order can be changed
                .optimizeWaypoints(false)
                .departureTime(departureTime);

        DirectionsResult results = googleMapsDelegate.execute(req);

        if (results.routes.length == 0) {
            log.warn("No routes found for origin={}, destination={}, waypoints={}", origin, destination, waypoints);
            throw new InvalidItineraryException();
        }

        DirectionsRoute route = results.routes[0];

        int numberOfLegs = route.legs.length;
        // waypoints + origin + destination - 1 (destination just finishes the last leg)
        int numberOfExpectedLegs = waypoints.length + 2 - 1;
        if (numberOfLegs != numberOfExpectedLegs) {
            // TODO: throw a proper exception...
            log.warn("Inconsistent number of legs. Expecting {} but got {}.", numberOfExpectedLegs, numberOfLegs);
            throw new IllegalStateException("Inconsistent number of legs.");
        } else {
            List<DirectionLeg> legs =
                Arrays.asList(route.legs)
                    .stream()
                    .peek(e -> log.info("leg {} distance {} eta {}. {}", "", e.distance, e.duration, e))
                    .map(directionsLeg -> DirectionLeg.builder()
                        .distance(Distance.ofMeters(directionsLeg.distance.inMeters))
                        .duration(Duration.ofSeconds(directionsLeg.duration.inSeconds))
                        .build()
                    )
                    .collect(Collectors.toList())
                ;
            return legs;
        }
    }

}
