package pt.famility.backoffice.web.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.service.dto.LogDTO;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class LoggerResource {

    private final Logger log = LoggerFactory.getLogger(LoggerResource.class);
    private static final String ERROR_PREFIX = "FrontEnd logger:  {} {}";

    @PostMapping("/logger")

    @ResponseStatus(HttpStatus.OK)
    public void log(@Valid @RequestBody LogDTO logDTO) {
        switch (logDTO.getLevel()) {
            case 0:
                log.trace(ERROR_PREFIX, logDTO.getMessage(), logDTO.getAdditional());
                break;
            case 1:
                log.debug(ERROR_PREFIX, logDTO.getMessage(), logDTO.getAdditional());
                break;
            case 2:
            case 3:
                log.info(ERROR_PREFIX, logDTO.getMessage(), logDTO.getAdditional());
                break;
            case 4:
                log.warn(ERROR_PREFIX, logDTO.getMessage(), logDTO.getAdditional());
                break;
            case 5:
            case 6:
                log.error(ERROR_PREFIX, logDTO.getMessage(), logDTO.getAdditional());
                break;
             default:
                log.info(ERROR_PREFIX, logDTO.getMessage(), logDTO.getAdditional());
        }
    }

}
