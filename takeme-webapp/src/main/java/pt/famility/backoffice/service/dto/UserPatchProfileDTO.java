package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.UserType;

import java.util.List;

@Data
@NoArgsConstructor
public class UserPatchProfileDTO {

    private List<UserType> types;

    private Long driverId;
}
