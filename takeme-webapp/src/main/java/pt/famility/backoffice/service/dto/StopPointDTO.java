package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.Itinerary;
import pt.famility.backoffice.domain.StopAuditEvent;
import pt.famility.backoffice.domain.enumeration.StopPointType;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class StopPointDTO {

    private Long id;
    private StopPointType stopPointType;
    private Instant scheduledTime;
    private Instant estimatedArriveTime;
    private Instant effectiveArriveTime;
    private LocationDTO location;
    private Itinerary itinerary;
    private List<ChildDTO> childList;
    private List<StopAuditEventDTO> stopAuditEvents;
}
