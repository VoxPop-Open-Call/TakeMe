package pt.famility.backoffice.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import pt.famility.backoffice.domain.Child;
import pt.famility.backoffice.domain.enumeration.OrganizationType;
import pt.famility.backoffice.domain.enumeration.StatusType;
import pt.famility.backoffice.repository.ChildRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.service.ChildService;
import pt.famility.backoffice.service.ChildSubscriptionService;
import pt.famility.backoffice.service.dto.ChildDTO;
import pt.famility.backoffice.service.dto.ChildItinerarySubscriptionDTO;
import pt.famility.backoffice.service.dto.ChildPatchDTO;
import pt.famility.backoffice.service.dto.ChildSubscriptionChildViewDTO;
import pt.famility.backoffice.service.dto.PhotoDTO;
import pt.famility.backoffice.service.dto.TutorDTO;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.util.HeaderUtil;
import pt.famility.backoffice.web.rest.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.famility.backoffice.domain.Child}.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ChildResource {

    private final Logger log = LoggerFactory.getLogger(ChildResource.class);

    private static final String ENTITY_NAME = "child";

    private final ChildRepository childRepository;

    private final ChildService childService;

    private final ChildSubscriptionService childSubscriptionService;

    private final AccessValidator accessValidator;

    public ChildResource(
            ChildRepository childRepository,
            ChildService childService,
            ChildSubscriptionService childSubscriptionService,
            AccessValidator accessValidator
    ) {
        this.childRepository = childRepository;
        this.childService = childService;
        this.childSubscriptionService = childSubscriptionService;
        this.accessValidator = accessValidator;
    }

    // TODO: ADD PROPER VALIDATION AND SECURITY

    /**
     * {@code GET  /children/:id/photo/:photoFileId} : get the "photoFileId" photo of "id" child.
     *
     * @param id          the child's "id".
     * @param photoFileId the photo's "id".
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the photo content in body.
     */
    @GetMapping("/children/{id}/photo/{photoFileId}")
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<PhotoDTO> getPhoto(HttpServletResponse response, @PathVariable Long id, @PathVariable Long photoFileId) {
        log.debug("REST request to get the Photo {} of Child {}", photoFileId, id);
        accessValidator.canAccessChildPhoto(id);
        PhotoDTO photo = childService.getPhotoByChildAndPhotoFileId(id, photoFileId);
        response.setHeader("Cache-Control", "no-transform, public, max-age=31536000");
        response.setHeader("Pragma", "");
        return ResponseEntity.ok().body(photo);
    }

    @PatchMapping("/children/{id}/photo")
    @Secured({AuthoritiesConstants.TUTOR, AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<ChildDTO> updatePhoto(@PathVariable Long id, @RequestBody PhotoDTO photoDTO) throws IOException {
        log.debug("Update child photo: {}", id);
        accessValidator.canPatchChildPhoto(id);
        return ResponseEntity.ok().body(childService.updatePhoto(id, photoDTO));
    }

    /**
     * POST  /children : Create a new child.
     *
     * @param child the child to create
     * @return the ResponseEntity with status 201 (Created) and with body the new child, or with status 400 (Bad Request) if the child has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/children")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Child> createChild(@Valid @RequestBody Child child) throws URISyntaxException {
        log.debug("REST request to save Child : {}", child);
        if (child.getId() != null) {
            throw new BadRequestAlertException("A new child cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Child result = childRepository.save(child);
        return ResponseEntity.created(new URI("/api/children/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /children : Updates an existing child.
     *
     * @param child the child to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated child,
     * or with status 400 (Bad Request) if the child is not valid,
     * or with status 500 (Internal Server Error) if the child couldn't be updated
     */
    @PutMapping("/children")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Child> updateChild(@Valid @RequestBody Child child) {
        log.debug("REST request to update Child : {}", child);
        if (child.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Child result = childRepository.save(child);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, child.getId().toString()))
                .body(result);
    }

    /**
     * GET  /children : get all the children.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of children in body
     */
    @GetMapping("/children")
    @Secured(AuthoritiesConstants.FAMILITY)
    public List<Child> getAllChildren(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Children");
        return childRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /children/:id : get the "id" child.
     *
     * @param id the id of the child to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the child, or with status 404 (Not Found)
     */
    @GetMapping("/children/{id}")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Child> getChild(@PathVariable Long id) {
        log.debug("REST request to get Child : {}", id);
        Optional<Child> child = childRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(child);
    }

    /**
     * DELETE  /children/:id : delete the "id" child.
     *
     * @param id the id of the child to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/children/{id}")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> deleteChild(@PathVariable Long id) {
        log.debug("REST request to delete Child : {}", id);
        childRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping(value = "/children/{id}/child-subscriptions")
    @Secured({AuthoritiesConstants.TUTOR, AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<List<ChildSubscriptionChildViewDTO>> findAllByChildIdAndOrganizationType(@PathVariable Long id, @RequestParam(required = false) OrganizationType organizationType, @RequestParam(required = false) StatusType status, Pageable pageable) {
        log.debug("REST request to get child-subscriptions of child {}", id);
        accessValidator.canAccessChildSubscriptions(id);
        Page<ChildSubscriptionChildViewDTO> page = childSubscriptionService.findAllByChildIdAndOrganizationType(id, organizationType, status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/children/" + id + "/child-subscriptions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PatchMapping("/children/{id}")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<Void> patchChild(@PathVariable Long id, @RequestBody ChildPatchDTO childPatchDTO) {
        log.debug("Rest request to patch child {}", id);
        accessValidator.canPatchChild(id);
        childService.patchChild(id, childPatchDTO);
        return ResponseEntity.ok().build();
    }

    // TODO: ADD ACCESS VALIDATOR

    /**
     * {@code GET  /children/:id/tutors} : get all the tutors of child "id".
     *
     * @param id the child's "id".
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and list of tutors in body.
     */
    @GetMapping(value = "/children/{id}/tutors")
    @Secured({AuthoritiesConstants.BUS_COMPANY, AuthoritiesConstants.FAMILITY})
    public ResponseEntity<List<TutorDTO>> getTutorsByChildId(@PathVariable Long id) {
        log.debug("REST request to get all Tutors of Child {}", id);
        List<TutorDTO> tutors = childService.getTutorsByChildId(id);
        return ResponseEntity.ok().body(tutors);
    }

    @GetMapping(value = "/children/{id}/child-itinerary-subscriptions")
    public ResponseEntity<List<ChildItinerarySubscriptionDTO>> getItinerarySubscriptionsByChildId(@PathVariable Long id) {
        log.debug("Request to get ChildItinerarySubscriptions by ChildId : {}", id);
        List<ChildItinerarySubscriptionDTO> itinerarySubscriptions = childService.findItinerarySubscriptionsByChildId(id);
        return ResponseEntity.ok().body(itinerarySubscriptions);
    }
}
