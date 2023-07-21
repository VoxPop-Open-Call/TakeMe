package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.ItineraryStatusType;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class ItineraryDTO {

    private Long id;
    private String name;
    private Instant scheduledTime;
    private Instant effectiveStartTime;
    private Instant effectiveEndTime;
    private ItineraryStatusType itineraryStatusType;
    private Double estimatedKM;
    private Integer estimatedTotalTime;
    private Double ocupation;
    private LocationDTO estimatedStartLocation;
    private LocationDTO startLocation;
    private LocationDTO endLocation;
    private OrganizationDTO organization;
    private VehicleDTO vehicle;
    private DriverDTO driver;
    private List<ItineraryStopPointDTO> itineraryStopPointList;
    private PromoterItineraryDTO promoterItinerary;

}
