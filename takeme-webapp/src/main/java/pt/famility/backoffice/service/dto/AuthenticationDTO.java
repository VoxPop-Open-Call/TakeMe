package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class AuthenticationDTO {

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(min = 5, max = 8)
    private String password;

    private Boolean returnSecureToken;

    public AuthenticationDTO(@Email @Size(min = 5, max = 254) String email, @Size(min = 5, max = 8) String password, Boolean returnSecureToken) {
        this.email = email;
        this.password = password;
        this.returnSecureToken = returnSecureToken;
    }

}
