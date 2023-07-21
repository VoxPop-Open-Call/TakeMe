package pt.famility.backoffice.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.converter.VehicleConverter;
import pt.famility.backoffice.domain.Organization;
import pt.famility.backoffice.domain.Vehicle;
import pt.famility.backoffice.repository.VehicleRepository;
import pt.famility.backoffice.service.dto.VehicleDTO;
import pt.famility.backoffice.web.rest.errors.NotFoundAlertException;

@Service
@Transactional
public class VehicleService {

    private VehicleRepository vehicleRepository;

    private VehicleConverter vehicleConverter;


    public VehicleService(VehicleRepository vehicleRepository, VehicleConverter vehicleConverter) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleConverter = vehicleConverter;
    }

    public Vehicle findById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new NotFoundAlertException("Vehicle does not exist", "vehicle", "id"));

        return vehicle;
    }

    public Page<VehicleDTO> findAllVehiclesByOrganizationId(Long id, String designation, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnorePaths("organization.createdDate", "organization.lastModifiedDate")
            .withMatcher("designation", genericPropertyMatcher -> genericPropertyMatcher.contains().ignoreCase());
        Organization organization = new Organization().id(id);
        Vehicle example = new Vehicle().designation(designation).organization(organization);
        return vehicleRepository.findAll(Example.of(example, matcher), pageable).map(vehicleConverter::convert);
    }
}
