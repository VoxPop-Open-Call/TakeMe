package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.Organization;

@Data
@NoArgsConstructor
public class DriverDTO {

    private Long id;

    private String name;

    private String driverLicense;

    private Long organizationId;

    private Organization organization;

    private UserDTO user;

    private Long photoId;

    private String photo;
}
