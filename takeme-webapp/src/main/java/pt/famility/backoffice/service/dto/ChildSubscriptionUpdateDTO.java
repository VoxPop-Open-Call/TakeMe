package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.StatusType;

@Data
@NoArgsConstructor
public class ChildSubscriptionUpdateDTO {

    private String comments;

    private String additionalInformation;

    private StatusType statusType;

    private Long idTutor;

    private String cardNumber;
}
