package pt.famility.backoffice.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.ServiceStopPointDaysOfWeek;
import pt.famility.backoffice.service.dto.ServiceStopPointDaysOfWeekDTO;

@Component
public class ServiceStopPointDaysOfWeekConverter {

    private final ObjectMapper objectMapper;

    public ServiceStopPointDaysOfWeekConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ServiceStopPointDaysOfWeekDTO convert(ServiceStopPointDaysOfWeek serviceStopPointDaysOfWeek) {
        return objectMapper.convertValue(serviceStopPointDaysOfWeek, ServiceStopPointDaysOfWeekDTO.class);
    }

}
