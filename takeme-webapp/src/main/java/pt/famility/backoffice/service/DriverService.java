package pt.famility.backoffice.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.converter.DriverConverter;
import pt.famility.backoffice.domain.DocumentFile;
import pt.famility.backoffice.domain.Driver;
import pt.famility.backoffice.domain.Organization;
import pt.famility.backoffice.repository.DriverRepository;
import pt.famility.backoffice.service.dto.DriverDTO;
import pt.famility.backoffice.service.dto.PhotoDTO;
import pt.famility.backoffice.web.rest.errors.InternalServerErrorException;
import pt.famility.backoffice.web.rest.errors.InvalidIDException;
import pt.famility.backoffice.web.rest.errors.NotFoundAlertException;

import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
public class DriverService {

    private DriverRepository driverRepository;

    private DriverConverter driverConverter;

    private PhotoService photoService;

    public DriverService(DriverRepository driverRepository, DriverConverter driverConverter,PhotoService photoService ) {
        this.driverRepository = driverRepository;
        this.driverConverter = driverConverter;
        this.photoService = photoService;
    }

    public Driver findById(Long id) {
        Driver driver = driverRepository.findById(id)
            .orElseThrow(() -> new NotFoundAlertException("Driver does not exist", "driver", "id"));

        return driver;
    }

    public DriverDTO findDriverById(Long id) {
        return driverConverter.convert(findById(id));
    }

    public Page<DriverDTO> findAllDriversByOrganizationId(Long id, String name, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnorePaths("organization.createdDate", "organization.lastModifiedDate")
            .withMatcher("name", genericPropertyMatcher -> genericPropertyMatcher.contains().ignoreCase());
        Organization organization = new Organization().id(id);
        Driver example = new Driver().name(name).organization(organization);
        return driverRepository.findAll(Example.of(example, matcher), pageable).map(driverConverter::convert);
    }

    public Page<DriverDTO> findAllDriversByOrganizationIdWithoutUser(Long id, String name, Pageable pageable) {
        if (!Optional.ofNullable(name).isPresent()) {
            name = "";
        }

        return driverRepository.findAllByOrganization_IdAndNameContainingAndUserIdIsNull(id, name, pageable).map(driverConverter::convert);
    }

    public DriverDTO save(DriverDTO driverDTO){
        Driver driver = driverRepository.save(driverConverter.convert(driverDTO));

        Optional.ofNullable(driverDTO.getPhoto()).ifPresent(photo -> {
            try {
                DocumentFile newPhoto = photoService.savePhoto(photo);
                driver.setPhotoFile(newPhoto);
            } catch (IOException e) {
                throw new InternalServerErrorException(e.getMessage());
            }
        });

        return driverConverter.convert(driverRepository.save(driver));
    }

    public PhotoDTO getPhotoByDriverAndPhotoFileId(Long id, Long photoFileId) {
        return driverRepository.findByIdAndPhotoFileId(id, photoFileId)
            .map(driver -> photoService.getPhoto(driver.getPhotoFile()))
            .orElseThrow(() -> new InvalidIDException("driverPhoto"));
    }

    public Driver findByUserId(Long userId) {
        return driverRepository.findByUserId_Id(userId).orElse(null);
    }

}
