package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ChildSubscriptionRegisterDTO {

    @NotNull
    private Long childId;

    @NotNull
    private Long[] organizationIds;

    @NotNull
    private Long userId;
}
