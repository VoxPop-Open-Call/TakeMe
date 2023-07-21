package pt.famility.backoffice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.Location;
import pt.famility.backoffice.domain.enumeration.ItineraryStatusType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryStopPointChildViewDTO {
    private Long childId;

    private Long itineraryId;

    private String childName;

    private String itineraryName;

    private LocalDateTime collectionScheduledTime;

    private Location collectionLocation;

    private LocalDateTime deliverScheduledTime;

    private Location deliverLocation;

    private ItineraryStatusType itineraryStatusType;
}
