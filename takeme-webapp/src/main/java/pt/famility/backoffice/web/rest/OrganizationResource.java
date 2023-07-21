package pt.famility.backoffice.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.famility.backoffice.converter.LocationConverter;
import pt.famility.backoffice.domain.Location;
import pt.famility.backoffice.domain.Organization;
import pt.famility.backoffice.domain.enumeration.OrganizationType;
import pt.famility.backoffice.domain.enumeration.StatusType;
import pt.famility.backoffice.repository.OrganizationRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.service.ChildService;
import pt.famility.backoffice.service.ChildSubscriptionService;
import pt.famility.backoffice.service.DriverService;
import pt.famility.backoffice.service.OrganizationService;
import pt.famility.backoffice.service.ServiceService;
import pt.famility.backoffice.service.TutorService;
import pt.famility.backoffice.service.UserService;
import pt.famility.backoffice.service.VehicleService;
import pt.famility.backoffice.service.dto.ChildDTO;
import pt.famility.backoffice.service.dto.ChildSubscriptionDTO;
import pt.famility.backoffice.service.dto.DriverDTO;
import pt.famility.backoffice.service.dto.LocationDTO;
import pt.famility.backoffice.service.dto.OrganizationDTO;
import pt.famility.backoffice.service.dto.OrganizationUpdateStatusDTO;
import pt.famility.backoffice.service.dto.PhotoDTO;
import pt.famility.backoffice.service.dto.ServiceDTO;
import pt.famility.backoffice.service.dto.TutorDTO;
import pt.famility.backoffice.service.dto.TutorRegistrationByOrganizationDTO;
import pt.famility.backoffice.service.dto.VehicleDTO;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.util.HeaderUtil;
import pt.famility.backoffice.web.rest.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.famility.backoffice.domain.Organization}.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class OrganizationResource {

    private static final String ENTITY_NAME = "organization";

    private final OrganizationRepository organizationRepository;

    private final OrganizationService organizationService;

    private final ChildSubscriptionService childSubscriptionService;

    private final TutorService tutorService;

    private final ChildService childService;

    private final VehicleService vehicleService;

    private final DriverService driverService;

    private final UserService userService;

    private final ServiceService serviceService;

    private final LocationConverter locationConverter;

    private final AccessValidator accessValidator;

    public OrganizationResource(
            OrganizationRepository organizationRepository,
            OrganizationService organizationService,
            ChildSubscriptionService childSubscriptionService,
            TutorService tutorService,
            ChildService childService,
            VehicleService vehicleService,
            DriverService driverService,
            UserService userService,
            ServiceService serviceService,
            LocationConverter locationConverter,
            AccessValidator accessValidator
    ) {
        this.organizationRepository = organizationRepository;
        this.organizationService = organizationService;
        this.childSubscriptionService = childSubscriptionService;
        this.tutorService = tutorService;
        this.childService = childService;
        this.vehicleService = vehicleService;
        this.driverService = driverService;
        this.userService = userService;
        this.serviceService = serviceService;
        this.locationConverter = locationConverter;
        this.accessValidator = accessValidator;
    }

    /**
     * POST  /organizations : Create a new organization.
     *
     * @param organization the organization to create
     * @return the ResponseEntity with status 201 (Created) and with body the new organization, or with status 400 (Bad Request) if the organization has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/organizations")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Organization> createOrganization(@Valid @RequestBody Organization organization) throws URISyntaxException {
        log.debug("REST request to save Organization : {}", organization);
        if (organization.getId() != null) {
            throw new BadRequestAlertException("A new organization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Organization result = organizationService.save(organization);
        return ResponseEntity.created(new URI("/api/organizations/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /organizations : Updates an existing organization.
     *
     * @param organization the organization to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated organization,
     * or with status 400 (Bad Request) if the organization is not valid,
     * or with status 500 (Internal Server Error) if the organization couldn't be updated
     */
    @PutMapping("/organizations")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Organization> updateOrganization(@Valid @RequestBody Organization organization) {
        log.debug("REST request to update Organization : {}", organization);
        if (organization.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Organization result = organizationService.save(organization);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, organization.getId().toString()))
                .body(result);
    }


    /**
     * GET  /organizations/:id : get the "id" organization.
     *
     * @param id the id of the organization to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the organization, or with status 404 (Not Found)
     */
    @GetMapping("/organizations/{id}")
    @Secured({AuthoritiesConstants.TUTOR, AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<OrganizationDTO> getOrganization(@PathVariable Long id) {
        log.debug("REST request to get Organization : {}", id);
        accessValidator.canAccessOrganization(id);
        Optional<OrganizationDTO> organization = organizationService.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(organization);
    }

    /**
     * DELETE  /organizations/:id : delete the "id" organization.
     *
     * @param id the id of the organization to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/organizations/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        log.debug("REST request to delete Organization : {}", id);
        organizationRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping(value = "/organizations/without-child-subscriptions")

    @Secured({AuthoritiesConstants.TUTOR, AuthoritiesConstants.FAMILITY})
    public ResponseEntity<List<OrganizationDTO>> getActiveOrganizationsWithoutSubscriptionsActiveOrPending(@RequestParam Long childId, @RequestParam OrganizationType organizationType, Pageable pageable) {
        log.debug("REST request to get active organizations without subscriptions active or pending");
        accessValidator.canAccessOrganizationsWithoutSubscription(childId);
        Page<OrganizationDTO> page = organizationService.findAllActiveOrganizationsWithoutSubscriptionsActiveOrPending(childId, organizationType, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organizations/without-child-subscriptions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @param name
     * @param status
     * @param type
     * @param pageable
     * @return
     */
    @GetMapping(value = "/organizations")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<List<OrganizationDTO>> findAllByFilters(@RequestParam(required = false) String name,
                                                                  @RequestParam(required = false) StatusType status,
                                                                  @RequestParam(required = false) OrganizationType type,
                                                                  Pageable pageable) {
        log.debug("REST request to get all organizations by filters");
        Page<OrganizationDTO> page = organizationService.findAllByFilters(name, status, type, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organizations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(value = "/organizations/{id}/child-subscriptions")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<List<ChildSubscriptionDTO>> findAllByOrganizationIdAndStatusType(@PathVariable Long id,
                                                                                           @RequestParam StatusType status,
                                                                                           @RequestParam(required = false) String childName,
                                                                                           @RequestParam(required = false) Boolean familityOnly,
                                                                                           @RequestParam(required = false) String additionalInformation,
                                                                                           Pageable pageable,
                                                                                           @RequestParam(required = false) Boolean famility) {
        log.debug("REST request to get all child subscriptions by organization and status type");
        accessValidator.canPerformOrganizationRelatedAction(id);

        Page<ChildSubscriptionDTO> page = childSubscriptionService.findAllByOrganizationIdAndStatusType(id, status, childName, familityOnly, additionalInformation, pageable, famility);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organizations/" + id + "/child-subscriptions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PatchMapping("/organizations/{id}")

    @Transactional
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @Valid @RequestBody OrganizationUpdateStatusDTO organizationUpdateStatusDTO) {
        log.debug("Update exit register status: {}", id);
        organizationService.updateStatus(id, organizationUpdateStatusDTO);
        userService.updateAllUsersByOrganization(id, organizationUpdateStatusDTO.getStatus());
        if (StatusType.INACTIVE.equals(organizationUpdateStatusDTO.getStatus())) {
            childSubscriptionService.deactivateAllByOrganizationId(id);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/organizations/{id}/photo/{photoFileId}")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<PhotoDTO> getPhoto(HttpServletResponse response, @PathVariable Long id, @PathVariable Long photoFileId) {
        log.debug("Organization photo: {}", id);
        accessValidator.canPerformOrganizationRelatedAction(id);
        response.setHeader("Cache-Control", "no-transform, public, max-age=31536000");
        response.setHeader("Pragma", "");
        return ResponseEntity.ok().body(organizationService.getByIdAndPhotoFileId(id, photoFileId));
    }

    @PatchMapping("/organizations/{id}/photo")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<OrganizationDTO> updatePhoto(@PathVariable Long id, @RequestBody PhotoDTO photoDTO) throws IOException {
        log.debug("Update organization photo: {}", id);
        accessValidator.canPerformOrganizationRelatedAction(id);
        return ResponseEntity.ok().body(organizationService.updatePhoto(id, photoDTO));
    }

    @PostMapping("/organizations/{id}/tutor")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<TutorDTO> createTutorByOrganization(
            @PathVariable Long id,
            @Valid @RequestBody TutorRegistrationByOrganizationDTO tutorRegistrationByOrganizationDTO
    ) {
        log.debug("Creating a new tutor by organization: {}", tutorRegistrationByOrganizationDTO);
        accessValidator.canPerformOrganizationRelatedAction(id);
        return ResponseEntity.ok(tutorService.createTutorByOrganization(id, tutorRegistrationByOrganizationDTO));
    }

    @PostMapping("/organizations/{id}/tutor/{idTutor}/child")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<ChildDTO> createChildByOrganization(
            @PathVariable Long id,
            @PathVariable Long idTutor,
            @Valid @RequestBody ChildDTO childDTO
    ) throws Exception {
        log.debug("Create child for tutor by organization: {}", childDTO);
        accessValidator.canPerformOrganizationRelatedAction(id);
        return ResponseEntity.ok(childService.createChildByOrganization(id, idTutor, childDTO));
    }

    @GetMapping("/organizations/{id}/vehicles")

    @Secured({AuthoritiesConstants.BUS_COMPANY_DRIVER, AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<List<VehicleDTO>> getVehicles(@PathVariable Long id, @RequestParam(required = false) String designation, Pageable pageable) {
        log.debug("Get organization vehicles");
        accessValidator.canPerformOrganizationRelatedAction(id);
        Page<VehicleDTO> page = vehicleService.findAllVehiclesByOrganizationId(id, designation, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organizations/" + id + "/vehicles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/organizations/{id}/drivers")

    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<List<DriverDTO>> getDrivers(@PathVariable Long id, @RequestParam(required = false) String name, Pageable pageable) {
        log.debug("Get organization drivers");
        accessValidator.canPerformOrganizationRelatedAction(id);
        Page<DriverDTO> page = driverService.findAllDriversByOrganizationId(id, name, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organizations/" + id + "/drivers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/organizations/{id}/drivers/without-user")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<List<DriverDTO>> getDriversWithoutUser(@PathVariable Long id, @RequestParam(required = false) String name, Pageable pageable) {
        log.debug("Get organization drivers without user");
        Page<DriverDTO> page = driverService.findAllDriversByOrganizationIdWithoutUser(id, name, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organizations/" + id + "/drivers/no-user");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/organizations/{id}/child-subscription/{idChildSubscription}/services")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<List<ServiceDTO>> getServicesByOrganizationAndChild(
            @PathVariable Long id, @PathVariable Long idChildSubscription, Pageable pageable
    ) {
        log.debug("Get services by organization and child");
        accessValidator.canPerformOrganizationRelatedAction(id);
        Page<ServiceDTO> servicesByOrganizationAndChildSubscriptionPage = serviceService.getServicesByOrganizationAndChildSubscription(id, idChildSubscription, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
                servicesByOrganizationAndChildSubscriptionPage,
                "/organizations/" + id + "/child-subscription/" + idChildSubscription + "/services");
        return new ResponseEntity<>(servicesByOrganizationAndChildSubscriptionPage.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code POST  /organizations/{id}/locations} : Create a new location for organization "id".
     *
     * @param locationDTO the locationDTO to create.
     * @return the ResponseEntity with status 201 (Created) and with body the new location, or with status 400 (Bad Request) if the location has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/organizations/{id}/locations")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<Location> createLocationToOrganizationServices(@PathVariable Long id, @Valid @RequestBody LocationDTO locationDTO) throws URISyntaxException {
        log.debug("REST request to save Location {} for Organization {}", locationDTO, id);
        accessValidator.canPerformOrganizationRelatedAction(id);
        if (locationDTO.getId() != null) throw new BadRequestAlertException("A new location cannot already have an ID", ENTITY_NAME, "idexists");
        Location result = organizationService.addLocation(id, locationDTO);
        return ResponseEntity.created(new URI("/api/organizations/" + id + "/locations")).body(result);
    }

    /**
     * {@code GET  /organizations/:id/locations} : get all the locations of organization "id".
     *
     * @param id the organization's "id".
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and list of locations in body.
     */
    @GetMapping("/organizations/{id}/locations")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<List<LocationDTO>> getLocationsByOrganization(@PathVariable Long id, @RequestParam(required = false) String designation, Pageable pageable) {
        log.debug("REST request to get all Locations of Organization {}", id);
        accessValidator.canPerformOrganizationRelatedAction(id);
        List<LocationDTO> locations = new ArrayList<>();
        Page<Location> locationsPage = organizationService.getLocationsByOrganization(id, designation, pageable);
        locationsPage.getContent().forEach(location -> locations.add(locationConverter.convert(location)));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(locationsPage, "/organizations/" + id + "/locations");
        return new ResponseEntity<>(locations, headers, HttpStatus.OK);
    }

    /**
     * {@code DELETE  /organizations/:id/locations/:locationId} : delete the "locationId" location of "id" organization.
     *
     * @param id         the organization's id.
     * @param locationId the location's id.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organizations/{id}/locations/{locationId}")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id, @PathVariable Long locationId) {
        log.debug("REST request to delete Location {} of Organization {}", locationId, id);
        accessValidator.canDeleteOrganizationServiceLocation(id, locationId);
        try {
            organizationService.deleteOrganizationServiceLocation(id, locationId);
        } catch (DataIntegrityViolationException ce) {
            throw new BadRequestAlertException("This location is being used", "organization", "location-in-use");
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code GET  /organizations/{organizationId}/child-subscriptions/card-number/{cardNumber} : get child subscription
     * with given card name and organization id
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body ChildSubscriptionDTO}.
     */
    @GetMapping("/organizations/{organizationId}/child-subscriptions/card-number/{cardNumber}")
    @Secured({AuthoritiesConstants.FAMILITY})
    public ResponseEntity<ChildSubscriptionDTO> getChildSubscriptionByCardNumber(@PathVariable Long organizationId,
                                                                                 @PathVariable String cardNumber) {

        return ResponseEntity.ok(childSubscriptionService.getChildSubscriptionByCardNumber(organizationId, cardNumber));
    }
}
