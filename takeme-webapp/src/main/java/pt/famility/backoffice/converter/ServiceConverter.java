package pt.famility.backoffice.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.Service;
import pt.famility.backoffice.domain.ServiceStopPoint;
import pt.famility.backoffice.service.dto.ServiceDTO;
import pt.famility.backoffice.service.dto.ServiceStopPointDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceConverter {

    private final ObjectMapper objectMapper;

    private OrganizationConverter organizationConverter;
    private ServiceStopPointConverter serviceStopPointConverter;

    public ServiceConverter(ObjectMapper objectMapper, OrganizationConverter organizationConverter, ServiceStopPointConverter serviceStopPointConverter) {
        this.objectMapper = objectMapper;
        this.organizationConverter = organizationConverter;
        this.serviceStopPointConverter = serviceStopPointConverter;
    }

    public ServiceDTO convert(Service service) {
        ServiceDTO serviceDTO = objectMapper.convertValue(service, ServiceDTO.class);
        serviceDTO.setOrganization(organizationConverter.convert(service.getOrganization()));

        List<ServiceStopPointDTO> serviceStopPointDTOList = new ArrayList<>();
        for (ServiceStopPoint serviceStopPoint : service.getServiceStopPoints()) {
            serviceStopPointDTOList.add(serviceStopPointConverter.convert(serviceStopPoint));
        }

        serviceDTO.setServiceStopPoints(serviceStopPointDTOList);

        return serviceDTO;
    }

    public Service convert(ServiceDTO serviceDTO) {
        return objectMapper.convertValue(serviceDTO, Service.class);
    }
}
