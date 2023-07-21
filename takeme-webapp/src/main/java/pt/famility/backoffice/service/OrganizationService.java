package pt.famility.backoffice.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.converter.LocationConverter;
import pt.famility.backoffice.converter.OrganizationConverter;
import pt.famility.backoffice.domain.ChildSubscription;
import pt.famility.backoffice.domain.DocumentFile;
import pt.famility.backoffice.domain.Location;
import pt.famility.backoffice.domain.Organization;
import pt.famility.backoffice.domain.User;
import pt.famility.backoffice.domain.enumeration.OrganizationType;
import pt.famility.backoffice.domain.enumeration.StatusType;
import pt.famility.backoffice.repository.ChildSubscriptionRepository;
import pt.famility.backoffice.repository.LocationRepository;
import pt.famility.backoffice.repository.OrganizationRepository;
import pt.famility.backoffice.repository.UserRepository;
import pt.famility.backoffice.service.dto.AdditionalInformationEntryDTO;
import pt.famility.backoffice.service.dto.LocationDTO;
import pt.famility.backoffice.service.dto.OrganizationDTO;
import pt.famility.backoffice.service.dto.OrganizationUpdateStatusDTO;
import pt.famility.backoffice.service.dto.PhotoDTO;
import pt.famility.backoffice.web.rest.errors.InvalidIDException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Organization}.
 */
@Service
@Transactional
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final OrganizationConverter organizationConverter;

    private final ChildSubscriptionRepository childSubscriptionRepository;

    private final UserRepository userRepository;

    private final PhotoService photoService;

    private final LocationConverter locationConverter;

    private final LocationRepository locationRepository;

    public OrganizationService(
            OrganizationRepository organizationRepository,
            OrganizationConverter organizationConverter,
            ChildSubscriptionRepository childSubscriptionRepository,
            UserRepository userRepository,
            PhotoService photoService,
            LocationConverter locationConverter,
            LocationRepository locationRepository
    ) {
        this.organizationRepository = organizationRepository;
        this.organizationConverter = organizationConverter;
        this.childSubscriptionRepository = childSubscriptionRepository;
        this.userRepository = userRepository;
        this.photoService = photoService;
        this.locationConverter = locationConverter;
        this.locationRepository = locationRepository;
    }

    public Organization save(Organization organization) {
        organization.getContacts().forEach(contact -> contact.setOrganization(organization));
        return organizationRepository.save(organization);
    }

    public void updateStatus(Long id, OrganizationUpdateStatusDTO organizationUpdateStatusDTO) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new InvalidIDException("organizationService"));
        organization.setStatusType(organizationUpdateStatusDTO.getStatus());

        organizationRepository.save(organization);
    }

    public Optional<Organization> getByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidIDException("organizationService"));
        return organizationRepository.findById(user.getOrganizationId());
    }

    public Organization findById(Long id) {
        return organizationRepository.findById(id).orElseThrow(() -> new InvalidIDException("organizationService"));
    }

    public Page<OrganizationDTO> findAllActiveOrganizationsWithoutSubscriptionsActiveOrPending(Long childId, OrganizationType organizationType, Pageable pageable) {
        List<ChildSubscription> allByChildIdAndOrganizationType = childSubscriptionRepository.findAllByChild_IdAndOrganization_OrganizationTypeOrderByStatusTypeAsc(childId, organizationType);
        List<Long> notInList = new ArrayList<>();
        allByChildIdAndOrganizationType.forEach(childSubscription -> {
            if (StatusType.ACTIVE.equals(childSubscription.getStatusType()) || StatusType.PENDING.equals(childSubscription.getStatusType())) {
                notInList.add(childSubscription.getOrganization().getId());
            }
        });

        Page<Organization> allByStatusTypeAndOrganizationType;
        if (notInList.isEmpty()) {
            allByStatusTypeAndOrganizationType = organizationRepository.findAllByStatusTypeAndOrganizationType(StatusType.ACTIVE, organizationType, pageable);
        } else {
            allByStatusTypeAndOrganizationType = organizationRepository.findAllByStatusTypeAndOrganizationTypeAndIdNotIn(StatusType.ACTIVE, organizationType, notInList, pageable);
        }

        return allByStatusTypeAndOrganizationType.map(organizationConverter::convert);
    }

    @Transactional(readOnly = true)
    public Page<OrganizationDTO> findAllByFilters(String name, StatusType statusType, OrganizationType organizationType, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", genericPropertyMatcher -> genericPropertyMatcher.contains().ignoreCase())
                .withIgnorePaths("createdDate", "lastModifiedDate");
        Organization organization = new Organization().name(name).organizationType(organizationType).statusType(statusType);
        return organizationRepository.findAll(Example.of(organization, matcher), pageable).map(organizationConverter::convert);
    }

    public OrganizationDTO convert(Organization organization) {
        return organizationConverter.convert(organization);
    }

    public PhotoDTO getByIdAndPhotoFileId(Long id, Long photoFileId) {
        return organizationRepository.findByIdAndPhotoFileId(id, photoFileId)
                .map(organization -> photoService.getPhoto(organization.getPhotoFile()))
                .orElseThrow(() -> new InvalidIDException("organizationPhoto"));
    }

    public OrganizationDTO updatePhoto(Long id, PhotoDTO photoDTO) throws IOException {
        Organization organization = organizationRepository.findById(id).orElseThrow(() -> new InvalidIDException("organizationPhoto"));
        DocumentFile oldPhoto = organization.getPhotoFile();
        DocumentFile newPhoto = photoService.savePhoto(photoDTO.getPhoto());
        organization.setPhotoFile(newPhoto);
        organizationRepository.save(organization);
        Optional.ofNullable(oldPhoto).ifPresent(photoService::deletePhoto);
        return convert(organization);
    }

    public Optional<OrganizationDTO> findOneWithEagerRelationships(Long id) {
        Optional<Organization> organization = organizationRepository.findOneWithEagerRelationships(id);
        OrganizationDTO organizationDTO = null;
        if (organization.isPresent()) {
            organizationDTO = convert(organization.get());
        }
        return Optional.ofNullable(organizationDTO);
    }

    public Location addLocation(Long id, LocationDTO locationDTO) {
        Location location = locationConverter.convert(locationDTO);
        Organization organization = organizationRepository
                .findById(id)
                .orElseThrow(() -> new InvalidIDException("organizationService"));
        organization.getServiceLocations().add(location);
        organizationRepository.save(organization);
        return location;
    }

    public Page<Location> getLocationsByOrganization(Long id, String location, Pageable pageable) {
        return this.locationRepository
                .findAllLocationsOfOrganization(id, Optional.ofNullable(location).isPresent() ? "%" + location + "%" : "%", pageable);
    }

    public Organization getOrganizationByLocationId(Long locationId) {
        return organizationRepository.findByLocationId(locationId).orElseThrow(() -> new InvalidIDException("OrganizationService"));
    }

    public Long getOrganizationIdByUserId(Long userId) {
        return getByUserId(userId).map(Organization::getId).orElse(-1L);
    }

    public List<Long> getServiceLocationIdsByOrganizationId(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new InvalidIDException("OrganizationService"));
        List<Location> list = new ArrayList<>(organization.getServiceLocations());
        return list.stream().map(Location::getId).collect(Collectors.toList());
    }

    public void deleteOrganizationServiceLocation(Long organizationId, Long locationId) {
        organizationRepository
                .findById(organizationId)
                .orElseThrow(() -> new InvalidIDException("organizationService"))
                .getServiceLocations()
                .removeIf(location -> location.getId().equals(locationId));
        locationRepository.deleteById(locationId);
    }

    public List<AdditionalInformationEntryDTO> getAdditionalInformationEntries(Long organizationId, StatusType statusType) {
        return childSubscriptionRepository.getAdditionalInformationEntries(organizationId, statusType).stream()
                .map(AdditionalInformationEntryDTO::new)
                .collect(Collectors.toList());
    }
}
