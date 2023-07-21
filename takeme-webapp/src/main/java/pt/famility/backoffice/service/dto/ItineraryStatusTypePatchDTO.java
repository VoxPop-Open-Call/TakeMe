package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.ItineraryStatusType;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ItineraryStatusTypePatchDTO {

    @NotNull
    private ItineraryStatusType statusType;

    private Double latitude;

    private Double longitude;
}
