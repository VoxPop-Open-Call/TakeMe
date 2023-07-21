package pt.famility.backoffice.service;

import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pt.famility.backoffice.config.ApplicationProperties;
import pt.famility.backoffice.config.GoogleMapsProperties;
import pt.famility.backoffice.service.dto.geolocation.ItineraryDirections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class GoogleMapsServiceTest {

    @InjectMocks
    private GoogleMapsService service;

    @Mock
    private GeoApiContext geoApiContext;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private GoogleMapsDelegate googleMapsDelegate;

    @Mock
    private GoogleMapsProperties googleMapsProperties;

    @Mock(answer = Answers.RETURNS_SELF)
    private DirectionsApiRequest directionsApiRequest;

    private LatLng origin = mock(LatLng.class);
    private LatLng destination = mock(LatLng.class);
    private LatLng[] waypoints = new LatLng[] { origin, destination };

    private Instant departureTime = Instant.now();
    private Duration stopTimeByWaypoint = Duration.ofSeconds(1);

    @Before
    public void setUp() throws Exception {
        when(applicationProperties.getGoogleMaps()).thenReturn(googleMapsProperties);
        when(googleMapsProperties.isWithTraffic()).thenReturn(false);
        when(googleMapsDelegate.newDirectionsRequest(geoApiContext)).thenReturn(directionsApiRequest);
    }

    private DirectionsResult emptyResult() {
        DirectionsResult result = new DirectionsResult();
        result.routes = new DirectionsRoute[0];
        return result;
    }

    private ApiException apiException() {
        return ApiException.from("INVALID_REQUEST", "Dummy");
    }


    @Test
    public void testInvalidRoute() throws Exception {

        when(googleMapsDelegate.execute(directionsApiRequest)).thenReturn(emptyResult());

        Optional<ItineraryDirections> result =
            service.calculateItineraryETA(departureTime, stopTimeByWaypoint, origin, destination, waypoints);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void testApiError() throws Exception {

        when(googleMapsDelegate.execute(directionsApiRequest)).thenThrow(apiException());

        Optional<ItineraryDirections> result =
            service.calculateItineraryETA(departureTime, stopTimeByWaypoint, origin, destination, waypoints);

        assertThat(result.isPresent()).isFalse();
    }
}
