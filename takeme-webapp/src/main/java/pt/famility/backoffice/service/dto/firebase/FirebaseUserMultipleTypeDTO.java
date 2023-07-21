package pt.famility.backoffice.service.dto.firebase;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.UserType;

import java.util.List;

/**
 * DTO used only to create user with multiple types
 */
@Data
@NoArgsConstructor
public class FirebaseUserMultipleTypeDTO {

    private FirebaseUserDTO firebaseUser;

    private List<UserType> types;

}
