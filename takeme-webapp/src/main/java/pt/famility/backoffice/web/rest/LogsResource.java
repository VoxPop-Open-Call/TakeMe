package pt.famility.backoffice.web.rest;

import org.slf4j.Logger;
import org.springframework.security.access.annotation.Secured;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.web.rest.vm.LoggerVM;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/management")
public class LogsResource {

    private final Logger log = LoggerFactory.getLogger(LogsResource.class);

    @GetMapping("/logs")

    @Secured(AuthoritiesConstants.FAMILITY)
    public List<LoggerVM> getList() {
        log.debug("REST request to get managements logs");
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        return context.getLoggerList()
            .stream()
            .map(LoggerVM::new)
            .collect(Collectors.toList());
    }

    @PutMapping("/logs")
    @ResponseStatus(HttpStatus.NO_CONTENT)

    @Secured(AuthoritiesConstants.FAMILITY)
    public void changeLevel(@RequestBody LoggerVM jsonLogger) {
        log.debug("REST request to set managements log");
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger(jsonLogger.getName()).setLevel(Level.valueOf(jsonLogger.getLevel()));
    }
}
