package pt.famility.backoffice.service.dto.firebase;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Used to send information to Firebase Send Email Verification
 */

@Data
@NoArgsConstructor
public class SendEmailDTO {

    private String requestType;
    private String idToken;

    public SendEmailDTO(String requestType, String idToken) {
        this.requestType = requestType;
        this.idToken = idToken;
    }

}
