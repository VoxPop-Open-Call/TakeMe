package pt.famility.backoffice.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.StopPoint;
import pt.famility.backoffice.service.dto.StopPointDTO;

@Component
public class StopPointConverter {

    private final ObjectMapper objectMapper;

    public StopPointConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public StopPoint convert (StopPointDTO stopPointDTO) {
        return objectMapper.convertValue(stopPointDTO, StopPoint.class);
    }

    public StopPointDTO convert(StopPoint stopPoint) {
        return objectMapper.convertValue(stopPoint, StopPointDTO.class);
    }
}
