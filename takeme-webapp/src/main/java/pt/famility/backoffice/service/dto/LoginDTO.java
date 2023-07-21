package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO with Login information
 */

@Data
@NoArgsConstructor
public class LoginDTO {

    private String idToken;
    private String refreshToken;
    private Long expiresIn;
    private String kind;
    private String localId;
    private String email;
    private String displayName;
    private Boolean registered;

    private UserDTO user;

}
