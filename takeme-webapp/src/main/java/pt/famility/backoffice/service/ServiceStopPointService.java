package pt.famility.backoffice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.converter.ServiceStopPointConverter;
import pt.famility.backoffice.repository.ServiceStopPointRepository;
import pt.famility.backoffice.service.dto.ServiceStopPointDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link pt.famility.backoffice.domain.ServiceStopPoint}.
 */
@Slf4j
@Service
@Transactional
public class ServiceStopPointService {

    private final ServiceStopPointRepository serviceStopPointRepository;

    private final ServiceStopPointConverter serviceStopPointConverter;

    public ServiceStopPointService(ServiceStopPointRepository serviceStopPointRepository, ServiceStopPointConverter serviceStopPointConverter) {
        this.serviceStopPointRepository = serviceStopPointRepository;
        this.serviceStopPointConverter = serviceStopPointConverter;
    }

    public List<ServiceStopPointDTO> findByServiceId(Long serviceId) {
        return serviceStopPointRepository
                .findAllByService_Id(serviceId)
                .map(serviceStopPointConverter::convert)
                .collect(Collectors.toList());
    }
}
