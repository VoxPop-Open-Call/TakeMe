package pt.famility.backoffice.service.dto.firebase;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Returned from HttpClientErrorException
 */
@Data
@NoArgsConstructor
public class ErrorDetail {

    private String message;
    private String domain;
    private String reason;

}
