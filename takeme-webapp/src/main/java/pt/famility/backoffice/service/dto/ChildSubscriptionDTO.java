package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ChildSubscriptionDTO {

    private Long id;

    private String userName;

    private String userNif;

    private Long userId;

    private Long userPhotoId;

    private String childName;

    private String childNif;

    private Long childId;

    private Long childPhotoId;

    private boolean famility;

    private Instant subscriptionDate;

    private Instant activationDate;

    private Instant deactivationDate;

    private String additionalInformation;

    private String comments;

    private String cardNumber;

    private LocalDate childDateOfBirth;
}
