package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VehicleDTO {

    private Long id;

    private String designation;

    private String licensePlate;

    private Integer capacity;

    private Long organizationId;

}
