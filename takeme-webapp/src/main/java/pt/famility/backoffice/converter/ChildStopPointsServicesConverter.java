package pt.famility.backoffice.converter;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.domain.ChildItinerarySubscription;
import pt.famility.backoffice.repository.ServiceRepository;
import pt.famility.backoffice.service.dto.ChildStopPointsServicesDTO;
import pt.famility.backoffice.service.dto.ServiceStopPointDTO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class ChildStopPointsServicesConverter {

    private final ChildConverter childConverter;

    private final ServiceRepository serviceRepository;

    private final ServiceStopPointConverter serviceStopPointConverter;

    public ChildStopPointsServicesConverter(
        ChildConverter childConverter,
        ServiceRepository serviceRepository,
        ServiceStopPointConverter serviceStopPointConverter
    ) {
        this.childConverter = childConverter;
        this.serviceRepository = serviceRepository;
        this.serviceStopPointConverter = serviceStopPointConverter;
    }

    public ChildStopPointsServicesDTO convert(ChildItinerarySubscription childItinerarySubscription) {
        ChildStopPointsServicesDTO childStopPointsServicesDTO = new ChildStopPointsServicesDTO();
        childStopPointsServicesDTO.setChild(childConverter.convert(childItinerarySubscription.getChild()));
        List<ServiceStopPointDTO> serviceStopPointDTOList = new ArrayList<>();
        serviceRepository.findAllByChildItinerarySubscriptionPromoterItineraryIdAndChildItinerarySubscriptionIdAndEndDateIsAfterOrChildItinerarySubscriptionPromoterItineraryIdAndChildItinerarySubscriptionIdAndEndDateIsNull(
                childItinerarySubscription.getPromoterItinerary().getId(),
                childItinerarySubscription.getId(),
                Instant.now(),
                childItinerarySubscription.getPromoterItinerary().getId(),
                childItinerarySubscription.getId()
        ).forEach(service -> service.getServiceStopPoints().forEach(serviceStopPoint -> serviceStopPointDTOList.add(serviceStopPointConverter.convert(serviceStopPoint))));
        childStopPointsServicesDTO.setServiceStopPoints(serviceStopPointDTOList);
        return childStopPointsServicesDTO;
    }
}
