package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.StatusType;

@Data
@NoArgsConstructor
public class OrganizationUpdateStatusDTO {
    private StatusType status;
}
