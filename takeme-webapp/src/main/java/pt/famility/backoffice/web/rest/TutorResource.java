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
import org.springframework.web.bind.annotation.CrossOrigin;
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
import pt.famility.backoffice.converter.ItineraryStopPointChildViewConverter;
import pt.famility.backoffice.domain.ItineraryStopPointChildView;
import pt.famility.backoffice.domain.Tutor;
import pt.famility.backoffice.domain.enumeration.StatusType;
import pt.famility.backoffice.repository.ItineraryStopPointChildViewRepository;
import pt.famility.backoffice.repository.TutorRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.service.ChildService;
import pt.famility.backoffice.service.ChildSubscriptionService;
import pt.famility.backoffice.service.TutorService;
import pt.famility.backoffice.service.dto.ChildDTO;
import pt.famility.backoffice.service.dto.ChildPatchDTO;
import pt.famility.backoffice.service.dto.ItineraryStopPointChildViewDTO;
import pt.famility.backoffice.service.dto.PhotoDTO;
import pt.famility.backoffice.service.dto.TutorDTO;
import pt.famility.backoffice.service.dto.TutorUpdateDTO;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.errors.InvalidIDException;
import pt.famility.backoffice.web.rest.util.HeaderUtil;
import pt.famility.backoffice.web.rest.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link pt.famility.backoffice.domain.Tutor}.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class TutorResource {

    private final Logger log = LoggerFactory.getLogger(TutorResource.class);

    private static final String ENTITY_NAME = "tutor";

    private final TutorRepository tutorRepository;

    private final TutorService tutorService;

    private final ChildService childService;

    private final ChildSubscriptionService childSubscriptionService;

    private final ItineraryStopPointChildViewRepository itineraryStopPointChildViewRepository;

    private final ItineraryStopPointChildViewConverter itineraryStopPointChildViewConverter;

    private final AccessValidator accessValidator;

    public TutorResource(
            TutorRepository tutorRepository,
            TutorService tutorService,
            ChildService childService,
            ChildSubscriptionService childSubscriptionService,
            ItineraryStopPointChildViewRepository itineraryStopPointChildViewRepository,
            ItineraryStopPointChildViewConverter itineraryStopPointChildViewConverter,
            AccessValidator accessValidator
    ) {
        this.tutorRepository = tutorRepository;
        this.tutorService = tutorService;
        this.childService = childService;
        this.childSubscriptionService = childSubscriptionService;
        this.itineraryStopPointChildViewRepository = itineraryStopPointChildViewRepository;
        this.itineraryStopPointChildViewConverter = itineraryStopPointChildViewConverter;
        this.accessValidator = accessValidator;
    }

    /**
     * POST  /tutors : Create a new tutor.
     *
     * @param tutorDTO the tutor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tutor, or with status 400 (Bad Request) if the tutor has already an ID
     */
    @PostMapping("/tutors")

    @Secured({AuthoritiesConstants.TUTOR, AuthoritiesConstants.FAMILITY})
    public ResponseEntity<TutorDTO> createTutor(@RequestBody TutorDTO tutorDTO) {
        log.debug("REST request to save Tutor : {}", tutorDTO);
        accessValidator.userCanCreateTutor(tutorDTO.getUserId());
        if (tutorDTO.getId() != null) {
            throw new BadRequestAlertException("A new tutor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tutorDTO.setFamility(true);
        tutorDTO.setStatusType(StatusType.ACTIVE);
        return new ResponseEntity(tutorService.save(tutorDTO), HttpStatus.CREATED);
    }

    /**
     * Verify if tutor exists
     *
     * @param userId
     * @return
     */
    @GetMapping("/tutors/exists/{userId}")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<TutorDTO> tutorExists(@PathVariable Long userId) {
        log.debug("Tutor exists: {}", userId);
        return ResponseEntity.ok().body(tutorService.findTutorDTOByUserId(userId));
    }

    // TODO: ADD PROPER VALIDATION AND SECURITY

    /**
     * {@code GET  /tutors/:id/photo/:photoFileId} : get the "photoFileId" photo of "id" tutor.
     *
     * @param id          the tutor's "id".
     * @param photoFileId the photo's "id".
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the photo content in body.
     */
    @GetMapping("/tutors/{id}/photo/{photoFileId}")
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<PhotoDTO> getPhoto(HttpServletResponse response, @PathVariable Long id, @PathVariable Long photoFileId) {
        log.debug("REST request to get the Photo {} of Tutor {}", photoFileId, id);
        accessValidator.canAccessTutorPhoto(id);
        PhotoDTO photo = tutorService.getPhotoByTutorAndPhotoFileId(id, photoFileId);
        response.setHeader("Cache-Control", "no-transform, public, max-age=31536000");
        response.setHeader("Pragma", "");
        return ResponseEntity.ok().body(photo);
    }

    @PatchMapping("/tutors/{id}/photo")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<TutorDTO> updatePhoto(@PathVariable Long id, @RequestBody PhotoDTO photoDTO) throws IOException {
        log.debug("Update tutor photo: {}", id);
        accessValidator.canPatchTutorPhoto(id);
        return ResponseEntity.ok().body(tutorService.updatePhoto(id, photoDTO));
    }

    /**
     * {@code GET  /tutors/:id/children} : get all the children of tutor "id".
     *
     * @param id the tutor's "id".
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and list of children in body.
     */
    @GetMapping("/tutors/{id}/children")
    @Secured({AuthoritiesConstants.TUTOR, AuthoritiesConstants.BUS_COMPANY, AuthoritiesConstants.FAMILITY})
    public ResponseEntity<List<ChildDTO>> getChildrenByTutor(@PathVariable Long id, @SortDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        log.debug("REST request to get all Children of Tutor {}", id);
        accessValidator.canPerformTutorAction(id);
        Page<ChildDTO> children = childService.getTutorChildrenByTutorId(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(children, "/tutors/" + id + "/children");
        return ResponseEntity.ok().headers(headers).body(children.getContent());
    }

    @GetMapping("/tutors/{tutorId}/children/{childId}")
    @Secured({AuthoritiesConstants.TUTOR, AuthoritiesConstants.FAMILITY})
    public ChildDTO getChildByTutorIdAndChildId(@PathVariable("tutorId") Long tutorId, @PathVariable("childId") Long childId) {
        log.debug("Child by tutor: {} and child: {}", tutorId, childId);
        accessValidator.canPerformTutorAction(tutorId);
        return childService.getChild(childId);
    }

    @PostMapping("/tutors/{id}/children")
    @Secured({AuthoritiesConstants.TUTOR, AuthoritiesConstants.FAMILITY})
    public ResponseEntity<ChildDTO> createChildrenByTutor(@PathVariable Long id, @Valid @RequestBody ChildDTO childDTO) throws IOException {
        log.debug("Create child for tutor: {}", id);
        accessValidator.canPerformTutorAction(id);
        childDTO.setFamility(true);
        childDTO.setStatusType(StatusType.ACTIVE);
        ChildDTO child = childService.save(id, childDTO);
        return new ResponseEntity(child, HttpStatus.CREATED);
    }

    @PatchMapping("/tutors/{tutorId}/children/{childId}")
    @Secured({AuthoritiesConstants.TUTOR, AuthoritiesConstants.FAMILITY})
    public ResponseEntity<ChildPatchDTO> updateChildrenByTutor(@PathVariable("tutorId") Long tutorId, @PathVariable("childId") Long childId, @Valid @RequestBody ChildPatchDTO childDTO) throws IOException {
        log.debug("Update child for tutor: {}", tutorId);
        accessValidator.canPerformTutorAction(tutorId);

        childDTO.setDateOfBirth(childDTO.getDateOfBirth().split("T")[0]);
        childService.patchChild(childId, childDTO);

        if (childDTO.getPhoto() != null) {
            PhotoDTO photo = new PhotoDTO();
            photo.setPhoto(childDTO.getPhoto());
            childService.updatePhoto(childId, photo);
        }

        childSubscriptionService.updateChildSubscriptionByTutor(childId);

        return new ResponseEntity(childDTO, HttpStatus.CREATED);
    }

    /**
     * PUT  /tutors : Updates an existing tutor.
     *
     * @param tutor the tutor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tutor,
     * or with status 400 (Bad Request) if the tutor is not valid,
     * or with status 500 (Internal Server Error) if the tutor couldn't be updated
     */
    @PutMapping("/tutors")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Tutor> updateTutor(@Valid @RequestBody Tutor tutor) {
        log.debug("REST request to update Tutor : {}", tutor);
        if (tutor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Tutor result = tutorRepository.save(tutor);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tutor.getId().toString()))
                .body(result);
    }

    @GetMapping("/tutors")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<List<TutorDTO>> getTutorsByFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) StatusType status,
            @RequestParam(required = false) Boolean isFamility,
            @RequestParam(required = false) Long idOrganization,
            @RequestParam(required = false) Boolean activated,
            Pageable pageable
    ) {
        log.debug("REST request to get tutors by filter");
        accessValidator.canAccessOrganizationTutors(idOrganization);
        Page<Tutor> page = tutorService.getTutorsByFilter(name, status, isFamility, idOrganization, activated, pageable);
        List<TutorDTO> tutorDTOList = new ArrayList<>();
        page.getContent().forEach(tutor -> tutorDTOList.add(tutorService.convert(tutor)));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tutors");
        return new ResponseEntity<>(tutorDTOList, headers, HttpStatus.OK);
    }

    /**
     * GET  /tutors/:id : get the "id" tutor.
     *
     * @param id the id of the tutor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tutor, or with status 404 (Not Found)
     */
    @GetMapping("/tutors/{id}")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<TutorDTO> getTutor(@PathVariable Long id) {
        log.debug("REST request to get Tutor : {}", id);
        accessValidator.canAccessTutor(id);
        Optional<Tutor> tutor = tutorRepository.findById(id);
        Optional<TutorDTO> tutorDTO = tutor.map(tutorService::convert);
        return ResponseUtil.wrapOrNotFound(tutorDTO);
    }

    /**
     * DELETE  /tutors/:id : delete the "id" tutor.
     *
     * @param id the id of the tutor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tutors/{id}")

    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> deleteTutor(@PathVariable Long id) {
        log.debug("REST request to delete Tutor : {}", id);
        tutorRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PatchMapping("/tutors/{id}")

    @Secured({AuthoritiesConstants.TUTOR, AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<TutorDTO> updateStatus(@PathVariable Long id, @Valid @RequestBody TutorUpdateDTO tutorUpdateDTO) {
        log.debug("Update tutor status: {}", id);
        accessValidator.canPatchTutor(id);
        return ResponseEntity.ok().body(tutorService.updateTutor(id, tutorUpdateDTO));
    }

    @GetMapping("/tutors/itineraries/by-child")
    @Secured({AuthoritiesConstants.TUTOR})
    public ResponseEntity<List<ItineraryStopPointChildViewDTO>> getItinerariesByChildId(@RequestParam List<Long> childId, Pageable pageable) {
        log.debug("Get ItineraryStopPointChildViews by ChildId {}", childId);
        Page<ItineraryStopPointChildViewDTO> result = itineraryStopPointChildViewRepository.findByChildIdIn(childId, pageable).map(itineraryStopPointChildViewConverter::convert);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(result, "/tutors/itineraries/by-child");
        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }
}
