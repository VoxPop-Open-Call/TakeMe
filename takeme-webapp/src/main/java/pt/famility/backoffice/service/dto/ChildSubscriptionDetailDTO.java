package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.StatusType;

import java.time.Instant;

@Data
@NoArgsConstructor
public class ChildSubscriptionDetailDTO {

    private Long id;

    private StatusType statusType;

    private Boolean famility;

    private Instant subscriptionDate;

    private Instant activationDate;

    private Instant deactivationDate;

    private String comments;

    private String additionalInformation;

    private ChildDTO child;

    private TutorDTO tutor;

    private OrganizationDTO organization;

    private String cardNumber;
}
