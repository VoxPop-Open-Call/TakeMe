package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.OriginType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UserMessagingTokenDTO {

    @NotBlank
    private String token;

    @NotNull
    private OriginType origin;

    @NotNull
    private Long userId;
}
