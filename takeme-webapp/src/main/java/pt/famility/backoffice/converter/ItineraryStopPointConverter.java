package pt.famility.backoffice.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.ItineraryStopPoint;
import pt.famility.backoffice.service.dto.ItineraryStopPointDTO;
import pt.famility.backoffice.service.dto.StopAuditEventDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItineraryStopPointConverter {

    private final ObjectMapper objectMapper;

    private final StopAuditEventConverter stopAuditEventConverter;

    private final ChildConverter childConverter;

    public ItineraryStopPointConverter(
        ObjectMapper objectMapper,
        StopAuditEventConverter stopAuditEventConverter,
        ChildConverter childConverter
    ) {
        this.objectMapper = objectMapper;
        this.stopAuditEventConverter = stopAuditEventConverter;
        this.childConverter = childConverter;
    }

    public ItineraryStopPoint convert (ItineraryStopPointDTO itineraryStopPointDTO) {
        return objectMapper.convertValue(itineraryStopPointDTO, ItineraryStopPoint.class);
    }

    public ItineraryStopPointDTO convert(ItineraryStopPoint itineraryStopPoint) {

        // forcing Location load
        Hibernate.initialize(itineraryStopPoint.getStopPoint().getLocation());

        ItineraryStopPointDTO itineraryStopPointDTO = objectMapper.convertValue(itineraryStopPoint, ItineraryStopPointDTO.class);
        itineraryStopPointDTO.setItineraryId(itineraryStopPoint.getItinerary().getId());

        List<StopAuditEventDTO> stopAuditEventDTOList = new ArrayList<>();

        itineraryStopPointDTO.getStopPoint().setChildList(
            itineraryStopPoint.getStopPoint().getChildList()
            .stream()
            .map(childConverter::convert)
            .collect(Collectors.toList())
            );

        itineraryStopPoint.getStopPoint().getStopAuditEvents().forEach(
            stopAuditEvent -> {
                StopAuditEventDTO stopAuditEventDTO = stopAuditEventConverter.converter(stopAuditEvent);
                stopAuditEventDTO.setChild(childConverter.convert(stopAuditEvent.getChild()));
                stopAuditEventDTOList.add(stopAuditEventDTO);
            }
        );

        itineraryStopPointDTO.getStopPoint().setStopAuditEvents(stopAuditEventDTOList);

        return itineraryStopPointDTO;
    }
}
