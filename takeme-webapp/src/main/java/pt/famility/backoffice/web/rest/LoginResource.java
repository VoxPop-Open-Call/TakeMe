package pt.famility.backoffice.web.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.service.LoginService;
import pt.famility.backoffice.service.dto.AuthenticationDTO;
import pt.famility.backoffice.service.dto.LoginDTO;
import pt.famility.backoffice.web.rest.errors.InvalidEmailException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class LoginResource {

    private final Logger log = LoggerFactory.getLogger(LoginResource.class);

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")

    @ResponseStatus(HttpStatus.OK)
    // @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<LoginDTO> login(@Valid @RequestBody AuthenticationDTO authenticationDTO) {
        log.debug("Login {}", authenticationDTO.getEmail());
        return ResponseEntity.ok(loginService.login(authenticationDTO));
    }

    @ExceptionHandler({InvalidEmailException.class})
    void handleInvalidEmailException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.FORBIDDEN.value());
    }

}
