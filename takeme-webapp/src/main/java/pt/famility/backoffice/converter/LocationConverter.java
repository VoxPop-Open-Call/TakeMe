package pt.famility.backoffice.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.Location;
import pt.famility.backoffice.service.dto.LocationDTO;

@Component
public class LocationConverter {

    private final ObjectMapper objectMapper;

    public LocationConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public LocationDTO convert(Location location) {
        return objectMapper.convertValue(location, LocationDTO.class);
    }

    public Location convert(LocationDTO locationDTO) {
        return  objectMapper.convertValue(locationDTO, Location.class);
    }

}
