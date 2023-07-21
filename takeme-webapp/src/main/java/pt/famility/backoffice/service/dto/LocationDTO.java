package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.LocationType;

@Data
@NoArgsConstructor
public class LocationDTO {
    private Long id;

    private String designation;

    private String street;

    private String portNumber;

    private String floor;

    private String postalCode;

    private String city;

    private String country;

    private LocationType type;

    private String longitude;

    private String latitude;

    private String plusCode;
}
