package pt.famility.backoffice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pt.famility.backoffice.domain.Configuration;
import pt.famility.backoffice.repository.ConfigurationRepository;
import pt.famility.backoffice.web.rest.errors.InvalidUIDException;

@Service
public class ConfigurationService {

    private ConfigurationRepository configurationRepository;

    public ConfigurationService(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    public String getValue(String key) {
        return configurationRepository.findById(key).map(configuration -> configuration.getValue()).orElseThrow(() -> new InvalidUIDException("configurationService"));
    }

    public Configuration getConfiguration(String key) {
        return configurationRepository.findById(key).orElseThrow(() -> new InvalidUIDException("configurationService"));
    }

    public Configuration save(Configuration configuration) {
        return configurationRepository.save(configuration);
    }

    public Page<Configuration> findAll(Pageable pageable) {
        return configurationRepository.findAll(pageable);
    }
}
