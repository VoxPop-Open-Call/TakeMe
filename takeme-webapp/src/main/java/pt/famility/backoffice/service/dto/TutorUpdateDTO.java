package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.StatusType;

@Data
@NoArgsConstructor
public class TutorUpdateDTO {

    private StatusType status;

    private String phoneNumber;

    private String photo;

    private Long photoId;

    private String firstName;

    private String lastName;

    private String identificationCardNumber;

    private String identificationCardTypeName;

}
