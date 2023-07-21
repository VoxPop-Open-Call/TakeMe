package pt.famility.backoffice.service.dto.firebase;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.UserType;
import pt.famility.backoffice.service.dto.UserDTO;

/**
 * DTO used only in login
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FirebaseUserDTO extends UserDTO {

    private String password;
    private UserType type;

}


