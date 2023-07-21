package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.ItineraryStatusType;

import java.time.Instant;

@Data
@NoArgsConstructor
public class ItineraryStopPointDTO {

    private Long id;
    private Instant estimatedTime;
    private Integer order;
    private StopPointDTO stopPoint;
    private Long itineraryId;

    private ItineraryStatusType itineraryStatusType;
}
