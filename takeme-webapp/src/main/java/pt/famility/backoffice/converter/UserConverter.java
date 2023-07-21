package pt.famility.backoffice.converter;

import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.User;
import pt.famility.backoffice.repository.DriverRepository;
import pt.famility.backoffice.repository.TutorRepository;
import pt.famility.backoffice.service.OrganizationService;
import pt.famility.backoffice.service.dto.UserDTO;

import java.util.Optional;

@Component
public class UserConverter {

    private OrganizationService organizationService;

    private TutorRepository tutorRepository;

    private TutorConverter tutorConverter;

    private DriverRepository driverRepository;

    private DriverConverter driverConverter;

    public UserConverter(
        OrganizationService organizationService,
        TutorRepository tutorRepository,
        TutorConverter tutorConverter,
        DriverRepository driverRepository,
        DriverConverter driverConverter
    ) {
        this.organizationService = organizationService;
        this.tutorRepository = tutorRepository;
        this.tutorConverter = tutorConverter;
        this.driverRepository = driverRepository;
        this.driverConverter = driverConverter;
    }

    public UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO(user);
        Optional.ofNullable(user.getOrganizationId()).ifPresent(organizationId -> userDTO.setOrganization(organizationService.convert(organizationService.findById(organizationId))));
        tutorRepository.findByUserId(user.getId()).ifPresent(tutor -> userDTO.setTutor(tutorConverter.convert(tutor)));
        driverRepository.findByUserId_Id(user.getId()).ifPresent(driver -> {
            userDTO.setDriver(driverConverter.convert(driver));
            userDTO.setDriverId(driver.getId());
        });
        return userDTO;
    }
}
