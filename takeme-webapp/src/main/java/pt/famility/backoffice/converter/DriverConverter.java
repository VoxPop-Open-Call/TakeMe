package pt.famility.backoffice.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.Driver;
import pt.famility.backoffice.service.dto.DriverDTO;

import java.util.Optional;

@Component
public class DriverConverter {

    private ObjectMapper objectMapper;

    public DriverConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public DriverDTO convert(Driver driver) {
        DriverDTO driverDTO = objectMapper.convertValue(driver, DriverDTO.class);
        Optional.ofNullable(driver.getPhotoFile()).ifPresent(documentFile -> driverDTO.setPhotoId(documentFile.getId()));
        driverDTO.setOrganizationId(driver.getOrganization().getId());

        return driverDTO;
    }

    public Driver convert(DriverDTO driverDTO) {
        return objectMapper.convertValue(driverDTO, Driver.class);
    }
}
