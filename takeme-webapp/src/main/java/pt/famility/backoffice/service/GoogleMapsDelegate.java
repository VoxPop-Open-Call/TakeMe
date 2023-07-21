package pt.famility.backoffice.service;

import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DistanceMatrix;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Abstracts all the static or final methods from GoogleMapsAPI (so we can test them)
 */
@Service
public class GoogleMapsDelegate {

    public DirectionsApiRequest newDirectionsRequest(GeoApiContext context) {
        return DirectionsApi.newRequest(context);
    }

    public DistanceMatrixApiRequest newDistanceMatrixRequest(GeoApiContext context) {
        return DistanceMatrixApi.newRequest(context);
    }

    public DirectionsResult execute(DirectionsApiRequest req) throws InterruptedException, ApiException, IOException {
        return req.await();
    }

    public DistanceMatrix execute(DistanceMatrixApiRequest req) throws InterruptedException, ApiException, IOException {
        return req.await();
    }

}
