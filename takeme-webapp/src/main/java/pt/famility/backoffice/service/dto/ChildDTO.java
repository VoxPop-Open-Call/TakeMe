package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.StatusType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ChildDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String nifNumber;

    @NotNull
    private LocalDate dateOfBirth;

    private Boolean famility;

    private StatusType statusType;

    private String nifCountry;

    private String photo;

    private Long photoId;

    private List<ChildSubscriptionChildViewDTO> activeSubscriptions = new ArrayList<>();

}
