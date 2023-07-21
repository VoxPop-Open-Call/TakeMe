package pt.famility.backoffice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import pt.famility.backoffice.converter.ServiceConverter;
import pt.famility.backoffice.domain.Service;
import pt.famility.backoffice.repository.ServiceRepository;
import pt.famility.backoffice.service.dto.ServiceDTO;
import pt.famility.backoffice.web.rest.errors.InvalidIDException;

import java.time.Instant;

/**
 * Service Implementation for managing {@link Service}.
 */
@Slf4j
@org.springframework.stereotype.Service
@Transactional
public class ServiceService {

    private final ServiceRepository serviceRepository;

    private final ServiceConverter serviceConverter;

    private final ChildItinerarySubscriptionService childItinerarySubscriptionService;

    private final ServiceStopPointService serviceStopPointService;

    public ServiceService(
            ServiceRepository serviceRepository,
            ServiceConverter serviceConverter,
            ChildItinerarySubscriptionService childItinerarySubscriptionService,
            ServiceStopPointService serviceStopPointService
    ) {
        this.serviceRepository = serviceRepository;
        this.serviceConverter = serviceConverter;
        this.childItinerarySubscriptionService = childItinerarySubscriptionService;
        this.serviceStopPointService = serviceStopPointService;
    }

    public Page<ServiceDTO> getServicesByOrganizationAndChildSubscription(Long idOrganization, Long idChildSubscription, Pageable pageable) {
        return serviceRepository
                .findAllByOrganizationIdAndChildItinerarySubscriptionIdAndEndDateIsAfterOrOrganizationIdAndChildItinerarySubscriptionIdAndEndDateIsNull
                        (idOrganization, idChildSubscription, Instant.now(), idOrganization, idChildSubscription, pageable)
                .map(serviceConverter::convert);
    }

    public ServiceDTO getServiceDetail(Long idService) {
        return serviceRepository
                .findById(idService)
                .map(service -> {
                    ServiceDTO serviceDTO = serviceConverter.convert(service);
                    Long subscriptionId = service.getChildItinerarySubscription().getId();
                    if (childItinerarySubscriptionService.findOne(subscriptionId).isPresent())
                        serviceDTO.setChildItinerarySubscription(childItinerarySubscriptionService.findOne(subscriptionId).get());
                    serviceDTO.setServiceStopPoints(serviceStopPointService.findByServiceId(service.getId()));
                    return serviceDTO;
                })
                .orElseThrow(() -> new InvalidIDException("getServiceDetail"));
    }

    public Service save(ServiceDTO serviceDTO) {
        return serviceRepository.save(serviceConverter.convert(serviceDTO));
    }

    public Page<ServiceDTO> getServicesBySubscriptionId(Long subscriptionId, Pageable pageable) {
        return serviceRepository
                .findAllByChildItinerarySubscriptionId(subscriptionId, pageable)
                .map(serviceConverter::convert);
    }
}
