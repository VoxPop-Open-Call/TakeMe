package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChildPatchDTO {

    private String name;

    private String dateOfBirth;

    private String nifNumber;

    private String photo;

    private String nifCountry;
}
