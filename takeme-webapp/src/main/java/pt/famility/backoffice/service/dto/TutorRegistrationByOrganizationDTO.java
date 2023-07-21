package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class TutorRegistrationByOrganizationDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private  String lastName;

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

    private String photo;
}
