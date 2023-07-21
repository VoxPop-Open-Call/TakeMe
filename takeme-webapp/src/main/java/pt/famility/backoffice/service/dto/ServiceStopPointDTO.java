package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.ServiceStopPointFrequency;
import pt.famility.backoffice.domain.enumeration.StatusType;
import pt.famility.backoffice.domain.enumeration.StopPointType;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
public class ServiceStopPointDTO {
    private Long id;
    private StopPointType stopPointType;
    private String startHour;
    private String combinedTime;
    private Instant startFrequencyDate;
    private StatusType statusType;
    private ServiceStopPointFrequency frequency;
    private LocationDTO location;
    private Long serviceId;
    private Set<ServiceStopPointDaysOfWeekDTO> serviceStopPointDaysOfWeeks;
}
