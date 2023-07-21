package pt.famility.backoffice.service.dto.firebase;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResetPasswordDTO {

    private String email;

    private String requestType = "PASSWORD_RESET";
}
