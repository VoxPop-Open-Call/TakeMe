package pt.famility.backoffice.web.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.famility.backoffice.config.AngularEnvironmentProperties;
import pt.famility.backoffice.config.ApplicationProperties;
import pt.famility.backoffice.domain.Configuration;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.service.ConfigurationService;
import pt.famility.backoffice.web.rest.util.HeaderUtil;
import pt.famility.backoffice.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(ConfigurationResource.class);

    private final String ENTITY_NAME = "configuration";

    private ConfigurationService configurationService;

    private ApplicationProperties applicationProperties;

    public ConfigurationResource(ConfigurationService configurationService, ApplicationProperties applicationProperties) {
        this.configurationService = configurationService;
        this.applicationProperties = applicationProperties;
    }

    @GetMapping("/configurations")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<List<Configuration>> findAll(Pageable pageable) {
        log.debug("Get configurations");
        Page<Configuration> page = configurationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/configurations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/configurations/{key}")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Configuration> getConfiguration(@PathVariable String key) {
        log.debug("Get configuration value: {}", key);
        return ResponseEntity.ok().body(configurationService.getConfiguration(key));
    }

    @PostMapping("/configurations")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Configuration> createConfiguration(@Valid @RequestBody Configuration configuration) throws URISyntaxException {
        log.debug("REST request to create configuration : {}", configuration);
        Configuration result = configurationService.save(configuration);
        return ResponseEntity.created(new URI("/api/configurations/" + result.getKey()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getKey()))
            .body(result);
    }

    @PutMapping("/configurations")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Configuration> updateConfiguration(@Valid @RequestBody Configuration configuration) {
        log.debug("REST request to update configuration : {}", configuration);
        Configuration result = configurationService.save(configuration);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/angular-env-configuration")
    public ResponseEntity<AngularEnvironmentProperties> getAngularEnvironmentConfigurationProperties() {
        log.debug("REST request to Angular Environment Configuration");
        return ResponseEntity.ok().body(applicationProperties.getAngularEnvironment());
    }

}
