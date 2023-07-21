package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.StatusType;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@NoArgsConstructor
public class TutorDTO {

    private Long id;

    private Long userId;

    private UserDTO user;

    @NotBlank
    private String name;

    @NotBlank
    private String nifCountry;

    @NotBlank
    private String nifNumber;

    @NotBlank
    private String identificationCardTypeName;

    @NotBlank
    private String identificationCardNumber;

    @NotBlank
    private String phoneNumber;

    private Boolean famility;

    private StatusType statusType;

    private Long photoId;

    private String photo;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

}
