package pt.famility.backoffice.service.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.Authority;
import pt.famility.backoffice.domain.Driver;
import pt.famility.backoffice.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
@Data
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @NotBlank
    @Size(min = 1, max = 50)
    @Email
    private String email;

    @Size(max = 256)
    private String imageUrl;

    private boolean activated = false;

    private Instant statusChangeDate;

    @Size(min = 2, max = 6)
    private String langKey;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<String> authorities;

    private String uid;

    private Long organizationId;

    private OrganizationDTO organization;

    private TutorDTO tutor;

    private Long driverId;

    private DriverDTO driver;

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.activated = user.getActivated();
        this.statusChangeDate = user.getStatusChangeDate();
        this.imageUrl = user.getImageUrl();
        this.langKey = user.getLangKey();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.authorities = user.getAuthorities().stream()
            .map(Authority::getName)
            .collect(Collectors.toSet());
        this.uid = user.getUid();
        this.organizationId = user.getOrganizationId();
    }
}
