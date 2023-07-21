package pt.famility.backoffice.web.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.famility.backoffice.converter.ItineraryConverter;
import pt.famility.backoffice.domain.Itinerary;
import pt.famility.backoffice.domain.enumeration.ItineraryStatusType;
import pt.famility.backoffice.repository.ItineraryRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.service.ItineraryService;
import pt.famility.backoffice.service.dto.ChildStopPointEventDTO;
import pt.famility.backoffice.service.dto.ItineraryDTO;
import pt.famility.backoffice.service.dto.ItineraryStatusTypePatchDTO;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.errors.NotFoundAlertException;
import pt.famility.backoffice.web.rest.util.HeaderUtil;
import pt.famility.backoffice.web.rest.util.PaginationUtil;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static pt.famility.backoffice.security.SecurityUtils.hasCurrentUserAnyOfAuthorities;

/**
 * REST controller for managing Itinerary.
 */
@RestController
@RequestMapping("/api")
public class ItineraryResource {

    private final Logger log = LoggerFactory.getLogger(ItineraryResource.class);

    private static final String ENTITY_NAME = "itinerary";

    private final ItineraryService itineraryService;

    private final ItineraryRepository itineraryRepository;

    private final ItineraryConverter itineraryConverter;

    private final AccessValidator accessValidator;

    public ItineraryResource(
        ItineraryRepository itineraryRepository,
        ItineraryService itineraryService,
        ItineraryConverter itineraryConverter,
        AccessValidator accessValidator
    ) {
        this.itineraryRepository = itineraryRepository;
        this.itineraryService = itineraryService;
        this.itineraryConverter = itineraryConverter;
        this.accessValidator = accessValidator;
    }

    /**
     * POST  /itineraries : Create a new itinerary.
     *
     * @param itineraryDTO the itinerary to create
     * @return the ResponseEntity with status 201 (Created) and with body the new itinerary, or with status 400 (Bad Request) if the itinerary has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/itineraries")

    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<ItineraryDTO> createItinerary(@Valid @RequestBody ItineraryDTO itineraryDTO) throws URISyntaxException {
        log.debug("REST request to save Itinerary : {}", itineraryDTO);
        accessValidator.canCreateItinerary(itineraryDTO.getOrganization().getId());
        if (itineraryDTO.getId() != null) {
            throw new BadRequestAlertException("A new itinerary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItineraryDTO resultItineraryDTO = itineraryService.createItinerary(itineraryDTO);
        return ResponseEntity.created(new URI("/api/itineraries/" + resultItineraryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, resultItineraryDTO.getId().toString()))
            .body(resultItineraryDTO);
    }

    /**
     * PUT  /itineraries : Updates an existing itinerary.
     *
     * @param itineraryDTO the itinerary to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated itinerary,
     * or with status 400 (Bad Request) if the itinerary is not valid,
     * or with status 500 (Internal Server Error) if the itinerary couldn't be updated
     */
    @PutMapping("/itineraries")

    @Secured({AuthoritiesConstants.BUS_COMPANY_DRIVER, AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<ItineraryDTO> updateItinerary(@Valid @RequestBody ItineraryDTO itineraryDTO) {
        log.debug("REST request to update Itinerary : {}", itineraryDTO);
        accessValidator.canUpdateItinerary(itineraryDTO);
        if (itineraryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ItineraryDTO itineraryDTOSaved = itineraryService.updateItinerary(itineraryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, itineraryDTOSaved.getId().toString()))
            .body(itineraryDTOSaved);
    }

    /**
     * GET  /itineraries : get all the itineraries.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of itineraries in body

    @GetMapping("/itineraries")

    @Secured(AuthoritiesConstants.FAMILITY)
    public List<Itinerary> getAllItineraries() {
        log.debug("REST request to get all Itineraries");
        return itineraryRepository.findAll();
    }*/

    /**
     * Webservice that returns a list of itineraries with pagination and filter by name and status
     *
     * @param id id of the organization to filter the itineraries
     * @param name name to filter the itineraries
     * @param statusTypes status of itinerary
     * @param pageable number of page, size of page and the way is sort
     * @return List with the itineraries
     */
    @GetMapping("/itineraries")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY, AuthoritiesConstants.TUTOR})
    public ResponseEntity<List<ItineraryDTO>> getItinerariesWithFilters(
        @RequestParam(required = false) Long id,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) List<ItineraryStatusType> statusTypes,
        @SortDefault(sort = "scheduledTime", direction = Sort.Direction.ASC)
        Pageable pageable
    ) {
        log.debug("Get itineraries with filters");
        if (!hasCurrentUserAnyOfAuthorities(AuthoritiesConstants.TUTOR)) {
            accessValidator.canPerformOrganizationRelatedAction(id);
        }
        Page<Itinerary> itineraryPage = itineraryService.getItinerariesWithFilters(id, name, statusTypes, pageable);
        List<ItineraryDTO> itineraryDTOList = new ArrayList<>();
        itineraryPage.getContent().forEach(itinerary -> itineraryDTOList.add(itineraryConverter.convert(itinerary)));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(itineraryPage, "/itineraries");
        return new ResponseEntity<>(itineraryDTOList, headers, HttpStatus.OK);
    }

    /**
     * GET  /itineraries/:id : get the "id" itinerary.
     *
     * @param id the id of the itinerary to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the itinerary, or with status 404 (Not Found)
     */
    @GetMapping("/itineraries/{id}")

    @Secured({AuthoritiesConstants.BUS_COMPANY_DRIVER, AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<ItineraryDTO> getItinerary(@PathVariable Long id) {
        log.debug("REST request to get Itinerary : {}", id);
        accessValidator.canAccessItineraryAndUpdateItineraryStatus(id);
        return ResponseEntity.ok(itineraryService.findById(id));
    }

    /**
     * DELETE  /itineraries/:id : delete the "id" itinerary.
     *
     * @param id the id of the itinerary to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/itineraries/{id}")

    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<Void> deleteItinerary(@PathVariable Long id) {
        log.debug("REST request to delete Itinerary : {}", id);
        accessValidator.canDeleteItinerary(id);
        itineraryService.deleteItinerary(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * PATCH /itineraries/:id/status : patch status of itinerary
     *
     * @param id itinerary id
     * @param itineraryStatusTypePatchDTO new status
     * @return if the request went well
     */
    @PatchMapping("/itineraries/{id}/status")
    @Secured({AuthoritiesConstants.BUS_COMPANY_DRIVER, AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<Void> patchStatusItinerary(@PathVariable Long id, @Valid @RequestBody ItineraryStatusTypePatchDTO itineraryStatusTypePatchDTO) {
        log.debug("Patch status of itinerary");
        accessValidator.canAccessItineraryAndUpdateItineraryStatus(id);
        itineraryService.patchItineraryStatus(id, itineraryStatusTypePatchDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * POST /itineraries/:id/finish-stop-point : Finish the current stopPoint with status of child to register
     *
     * @param id itinerary id
     * @param childStopPointEventDTOList list of child event to register
     * @return Itinerary
     */
    @PostMapping("/itineraries/{id}/finish-stop-point")
    @Secured({AuthoritiesConstants.BUS_COMPANY_DRIVER, AuthoritiesConstants.FAMILITY})
    public ResponseEntity<ItineraryDTO> finishItineraryStopPoint(@PathVariable Long id, @Valid @RequestBody List<ChildStopPointEventDTO> childStopPointEventDTOList) {
        log.debug("REST request to finish the current stop point itinerary with child events");
        accessValidator.canFinishItineraryStopPoint(id);
        return ResponseEntity.ok(itineraryService.finishItineraryStopPoint(id, childStopPointEventDTOList));
    }

    @ExceptionHandler({NotFoundAlertException.class})
    void handleNotFoundAlertException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

}
