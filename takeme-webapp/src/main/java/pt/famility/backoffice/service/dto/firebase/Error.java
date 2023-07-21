package pt.famility.backoffice.service.dto.firebase;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Error returned from HttpClientErrorException
 */
@Data
@NoArgsConstructor
public class Error {

    private Integer code;
    private String message;
    private List<ErrorDetail> errors;

}
