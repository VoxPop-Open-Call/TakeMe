package pt.famility.backoffice.service;

import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pt.famility.backoffice.config.ApplicationProperties;
import pt.famility.backoffice.config.GoogleMapsConfiguration;
import pt.famility.backoffice.service.dto.geolocation.DirectionLeg;
import pt.famility.backoffice.service.dto.geolocation.ItineraryDirections;
import pt.famility.backoffice.service.exception.InvalidItineraryException;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { GoogleMapsConfiguration.class, GoogleMapsService.class })
@EnableConfigurationProperties(ApplicationProperties.class)
@Slf4j
public class GoogleMapsServiceIntTest {

    @Autowired
    GoogleMapsService googleMapsService;

    @Test
    public void distanceTimeLisbon () throws InterruptedException, ApiException, IOException {

        LatLng source = new LatLng(38.7385305,-9.1517655);
        LatLng target = new LatLng(38.726115, -9.150161);

        long distanceTime = googleMapsService.distanceTime(source, target);

        Assert.assertTrue("Distance ", distanceTime > 0 );

    }

    @Test
    public void distanceTimeUnknown () throws InterruptedException, ApiException, IOException {
        // Azores
        LatLng source = new LatLng(37.753512,-25.699047);
        // Lisbon
        LatLng target = new LatLng(38.726115, -9.150161);

        long distanceTime = googleMapsService.distanceTime(source, target);

        Assert.assertEquals("Distance ", 0, distanceTime );

    }

    @Test
    public void etaWith4StopPoints () throws InterruptedException, ApiException, IOException {

        /*
         Calling Google Maps API to calculate the ETA between 38.73853050,-9.15176550 and 38.72611500,-9.15016100 thru [38.73666800,-9.15243670, 38.72936970,-9.14976380]. Traffic enabled? false
         leg  distance 1.1 km eta 5 mins. [DirectionsLeg: "Av. Elias Garcia 176A, 1050-181 Lisboa, Portugal" -> "R. Marquês Sá da Bandeira 76, 1050-165 Lisboa, Portugal" (38.73850320,-9.15175930 -> 38.73667060,-9.15240080), duration=5 mins, distance=1.1 km: 8 steps]
         leg  distance 1.0 km eta 4 mins. [DirectionsLeg: "R. Marquês Sá da Bandeira 76, 1050-165 Lisboa, Portugal" -> "Av. António Augusto de Aguiar 11, 1050-214 Lisboa, Portugal" (38.73667060,-9.15240080 -> 38.72936690,-9.14976900), duration=4 mins, distance=1.0 km: 3 steps]
         leg  distance 0.4 km eta 1 min. [DirectionsLeg: "Av. António Augusto de Aguiar 11, 1050-214 Lisboa, Portugal" -> "Marquês de Pombal, 1070-051 Lisboa, Portugal" (38.72936690,-9.14976900 -> 38.72610720,-9.15015600), duration=1 min, distance=0.4 km: 3 steps]
         */
        LatLng eliasGarcia176 = new LatLng(38.7385305,-9.1517655);
        LatLng marquesSaBandeira76 = new LatLng(38.73666800,-9.15243670);
        LatLng antonioAugustoAguiar11 = new LatLng(38.72936970,-9.14976380);
        LatLng marquesPombal = new LatLng(38.726115, -9.150161);

        LatLng source = eliasGarcia176;
        LatLng target = marquesPombal;

        List<DirectionLeg> directionLegs = calculateETA(source, target, marquesSaBandeira76, antonioAugustoAguiar11);

        Assert.assertEquals("Nr of legs ", 3, directionLegs.size());

    }


    private List<DirectionLeg> calculateETA(LatLng origin, LatLng destination, LatLng... waypoints)
        throws InterruptedException, ApiException, IOException {
        return googleMapsService.calculateETA(Instant.now(), origin, destination, waypoints);
    }

    @Test
    public void itineraryDirectionsWith4StopPoints () throws InterruptedException, ApiException, IOException {

        // GIVEN
        /*
         Calling Google Maps API to calculate the ETA between 38.73853050,-9.15176550 and 38.72611500,-9.15016100 thru [38.73666800,-9.15243670, 38.72936970,-9.14976380]. Traffic enabled? false
         leg  distance 1.1 km eta 5 mins. [DirectionsLeg: "Av. Elias Garcia 176A, 1050-181 Lisboa, Portugal" -> "R. Marquês Sá da Bandeira 76, 1050-165 Lisboa, Portugal" (38.73850320,-9.15175930 -> 38.73667060,-9.15240080), duration=5 mins, distance=1.1 km: 8 steps]
         leg  distance 1.0 km eta 4 mins. [DirectionsLeg: "R. Marquês Sá da Bandeira 76, 1050-165 Lisboa, Portugal" -> "Av. António Augusto de Aguiar 11, 1050-214 Lisboa, Portugal" (38.73667060,-9.15240080 -> 38.72936690,-9.14976900), duration=4 mins, distance=1.0 km: 3 steps]
         leg  distance 0.4 km eta 1 min. [DirectionsLeg: "Av. António Augusto de Aguiar 11, 1050-214 Lisboa, Portugal" -> "Marquês de Pombal, 1070-051 Lisboa, Portugal" (38.72936690,-9.14976900 -> 38.72610720,-9.15015600), duration=1 min, distance=0.4 km: 3 steps]
         */
        LatLng eliasGarcia176 = new LatLng(38.7385305,-9.1517655);
        LatLng marquesSaBandeira76 = new LatLng(38.73666800,-9.15243670);
        LatLng antonioAugustoAguiar11 = new LatLng(38.72936970,-9.14976380);
        LatLng marquesPombal = new LatLng(38.726115, -9.150161);

        LatLng source = eliasGarcia176;
        LatLng target = marquesPombal;

        // WHEN
        ItineraryDirections directionLegs = googleMapsService.calculateItineraryETA(
            Instant.now(), Duration.ofMinutes(3), source, target, marquesSaBandeira76, antonioAugustoAguiar11).get();

        // THEN
        log.info("ItineraryDirections: {}", directionLegs);
        final long ACTUAL = directionLegs.getTotalDistance().getDistanceInMeters();
        final long TOLERANCE = 10;
        final long EXPECTED = 2576;
        Assert.assertTrue("Total Distance " + ACTUAL + " is out of the expected value "
                + EXPECTED + " using a tolerance value of " + TOLERANCE,
            Math.abs(EXPECTED - ACTUAL) < TOLERANCE);
    }

    @Test(expected = InvalidItineraryException.class)
    public void itineraryWithUnknownDirections () throws InterruptedException, ApiException, IOException {

        // GIVEN
        LatLng s = new LatLng(1.00000000,1.00000000);
        LatLng p1 = new LatLng(1.00000000,1.00000000);
        LatLng t = new LatLng(2.00000000,2.00000000);

        LatLng source = s;
        LatLng target = t;

        // WHEN
        Assert.assertFalse(googleMapsService.calculateItineraryETA(Instant.now(), Duration.ofMinutes(3), source, target, p1).isPresent());

        // THEN invalid route
    }
}
