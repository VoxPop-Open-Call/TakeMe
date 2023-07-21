package pt.famility.backoffice.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.Itinerary;
import pt.famility.backoffice.service.dto.ItineraryDTO;

@Component
public class ItineraryConverter {

    private ObjectMapper objectMapper;

    public ItineraryConverter(
        ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
    }

    public ItineraryDTO convert(Itinerary itinerary) {

        return objectMapper.convertValue(itinerary, ItineraryDTO.class);
    }

    public Itinerary convert(ItineraryDTO itineraryDTO) {

        return objectMapper.convertValue(itineraryDTO, Itinerary.class);
    }
}
