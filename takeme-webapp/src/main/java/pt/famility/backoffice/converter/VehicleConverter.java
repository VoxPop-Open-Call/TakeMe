package pt.famility.backoffice.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.Vehicle;
import pt.famility.backoffice.service.dto.VehicleDTO;

@Component
public class VehicleConverter {

    private final ObjectMapper objectMapper;

    public VehicleConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public VehicleDTO convert(Vehicle vehicle) {
        VehicleDTO vehicleDTO = objectMapper.convertValue(vehicle, VehicleDTO.class);
        vehicleDTO.setOrganizationId(vehicle.getOrganization().getId());

        return vehicleDTO;
    }
}
