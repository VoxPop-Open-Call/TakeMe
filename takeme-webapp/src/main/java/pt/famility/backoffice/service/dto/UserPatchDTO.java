package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserPatchDTO {

    private String firstName;

    private String lastName;

    private String langKey;
}
