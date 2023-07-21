package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.Contact;
import pt.famility.backoffice.domain.enumeration.OrganizationType;
import pt.famility.backoffice.domain.enumeration.StatusType;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class OrganizationDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String nifCountry;

    @NotBlank
    private String nifNumber;

    private OrganizationType organizationType;

    private StatusType statusType;

    private LocationDTO location;

    private Long photoId;

    private String photo;

    private Set<Contact> contacts = new HashSet<>();

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;
}
