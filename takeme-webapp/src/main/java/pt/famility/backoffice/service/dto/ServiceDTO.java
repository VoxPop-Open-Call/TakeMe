package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class ServiceDTO {
    private Long id;
    private Boolean recurrent;
    private Instant startDate;
    private Instant endDate;
    private OrganizationDTO organization;
    private ChildItinerarySubscriptionDTO childItinerarySubscription;
    private List<ServiceStopPointDTO> serviceStopPoints;
}
