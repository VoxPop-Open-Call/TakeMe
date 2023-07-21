package pt.famility.backoffice.converter;

import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.ItineraryStopPointChildView;
import pt.famility.backoffice.service.dto.ItineraryStopPointChildViewDTO;

@Component
public class ItineraryStopPointChildViewConverter {

    public ItineraryStopPointChildViewConverter() {}

    public ItineraryStopPointChildViewDTO convert(ItineraryStopPointChildView itineraryStopPointChildView) {
        return new ItineraryStopPointChildViewDTO(
            itineraryStopPointChildView.getChildId(),
            itineraryStopPointChildView.getItineraryId(),
            itineraryStopPointChildView.getChildName(),
            itineraryStopPointChildView.getItineraryName(),
            itineraryStopPointChildView.getCollectionScheduledTime(),
            itineraryStopPointChildView.getCollectionLocation(),
            itineraryStopPointChildView.getDeliverScheduledTime(),
            itineraryStopPointChildView.getDeliverLocation(),
            itineraryStopPointChildView.getItineraryStatusType()
        );
    }

    public ItineraryStopPointChildView convert(ItineraryStopPointChildViewDTO itineraryStopPointChildViewDTO) {
        ItineraryStopPointChildView itineraryStopPointChildView = new ItineraryStopPointChildView();
        itineraryStopPointChildView.setChildId(itineraryStopPointChildViewDTO.getChildId());
        itineraryStopPointChildView.setItineraryId(itineraryStopPointChildViewDTO.getItineraryId());
        itineraryStopPointChildView.setChildName(itineraryStopPointChildViewDTO.getChildName());
        itineraryStopPointChildView.setItineraryName(itineraryStopPointChildViewDTO.getItineraryName());
        itineraryStopPointChildView.setCollectionScheduledTime(itineraryStopPointChildViewDTO.getCollectionScheduledTime());
        itineraryStopPointChildView.setCollectionLocation(itineraryStopPointChildViewDTO.getCollectionLocation());
        itineraryStopPointChildView.setDeliverScheduledTime(itineraryStopPointChildViewDTO.getDeliverScheduledTime());
        itineraryStopPointChildView.setDeliverLocation(itineraryStopPointChildViewDTO.getDeliverLocation());
        itineraryStopPointChildView.setItineraryStatusType(itineraryStopPointChildViewDTO.getItineraryStatusType());
        return itineraryStopPointChildView;
    }

}
