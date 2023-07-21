package pt.famility.backoffice.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.ServiceStopPoint;
import pt.famility.backoffice.service.dto.ServiceStopPointDTO;

import java.util.stream.Collectors;

@Component
public class ServiceStopPointConverter {

    private final ObjectMapper objectMapper;

    private LocationConverter locationConverter;

    private ServiceStopPointDaysOfWeekConverter serviceStopPointDaysOfWeekConverter;

    public ServiceStopPointConverter(ObjectMapper objectMapper, LocationConverter locationConverter, ServiceStopPointDaysOfWeekConverter serviceStopPointDaysOfWeekConverter) {
        this.objectMapper = objectMapper;
        this.locationConverter = locationConverter;
        this.serviceStopPointDaysOfWeekConverter = serviceStopPointDaysOfWeekConverter;
    }

    public ServiceStopPointDTO convert(ServiceStopPoint serviceStopPoint) {
        ServiceStopPointDTO serviceStopPointDTO = objectMapper.convertValue(serviceStopPoint, ServiceStopPointDTO.class);
        serviceStopPointDTO.setServiceId(serviceStopPoint.getService().getId());
        serviceStopPointDTO.setLocation(locationConverter.convert(serviceStopPoint.getLocation()));
        serviceStopPointDTO.setServiceStopPointDaysOfWeeks(serviceStopPoint.getServiceStopPointDaysOfWeeks().stream().map(serviceStopPointDaysOfWeekConverter::convert).collect(Collectors.toSet()));
        return serviceStopPointDTO;
    }
}
