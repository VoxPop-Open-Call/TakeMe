package pt.famility.backoffice.web.rest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;
import pt.famility.backoffice.domain.ChildSubscription;
import pt.famility.backoffice.repository.ChildSubscriptionRepository;
import pt.famility.backoffice.security.AccessValidator;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.service.ChildSubscriptionService;
import pt.famility.backoffice.service.dto.ChildSubscriptionDetailDTO;
import pt.famility.backoffice.service.dto.ChildSubscriptionRegisterDTO;
import pt.famility.backoffice.service.dto.ChildSubscriptionUpdateDTO;
import pt.famility.backoffice.service.dto.ImportDTO;
import pt.famility.backoffice.web.rest.errors.BadRequestAlertException;
import pt.famility.backoffice.web.rest.util.HeaderUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ChildSubscription.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ChildSubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(ChildSubscriptionResource.class);

    private static final String ENTITY_NAME = "childSubscription";

    private final ChildSubscriptionRepository childSubscriptionRepository;

    private final ChildSubscriptionService childSubscriptionService;

    private AccessValidator accessValidator;

    public ChildSubscriptionResource(ChildSubscriptionRepository childSubscriptionRepository, ChildSubscriptionService childSubscriptionService, AccessValidator accessValidator) {
        this.childSubscriptionRepository = childSubscriptionRepository;
        this.childSubscriptionService = childSubscriptionService;
        this.accessValidator = accessValidator;
    }

    /**
     * POST  /child-subscriptions : Create a new childSubscription.
     *
     * @param childSubscriptionRegisterDTO the childSubscription to create
     * @return the ResponseEntity with status 201 (Created) or with status 400 (Bad Request) if the childSubscription has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/child-subscriptions")
    @Secured({AuthoritiesConstants.TUTOR, AuthoritiesConstants.FAMILITY})
    public ResponseEntity<Void> createChildSubscription(@Valid @RequestBody ChildSubscriptionRegisterDTO childSubscriptionRegisterDTO) throws URISyntaxException {
        log.debug("REST request to save ChildSubscription : {}", childSubscriptionRegisterDTO);
        accessValidator.canCreateChildSubscription(childSubscriptionRegisterDTO.getChildId());
        childSubscriptionService.create(childSubscriptionRegisterDTO);
        return ResponseEntity.created(new URI("/api/child-subscriptions/")).build();
    }

    /**
     * PUT  /child-subscriptions : Updates an existing childSubscription.
     *
     * @param childSubscription the childSubscription to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated childSubscription,
     * or with status 400 (Bad Request) if the childSubscription is not valid,
     * or with status 500 (Internal Server Error) if the childSubscription couldn't be updated
     */
    @PutMapping("/child-subscriptions")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<ChildSubscription> updateChildSubscription(@Valid @RequestBody ChildSubscription childSubscription) {
        log.debug("REST request to update ChildSubscription : {}", childSubscription);
        if (childSubscription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChildSubscription result = childSubscriptionRepository.save(childSubscription);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, childSubscription.getId().toString()))
            .body(result);
    }

    @PatchMapping("/child-subscriptions/{id}")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<Void> updateChildSubscription(@PathVariable Long id, @Valid @RequestBody ChildSubscriptionUpdateDTO childSubscriptionUpdateDTO) {
        log.debug("REST request to update child subscription {}", id);
        accessValidator.canPatchChildSubscription(id);
        if (Optional.ofNullable(childSubscriptionUpdateDTO.getStatusType()).isPresent()) {
            childSubscriptionService.updateStatus(id, childSubscriptionUpdateDTO);
        }
        if (Optional.ofNullable(childSubscriptionUpdateDTO.getIdTutor()).isPresent()) {
            childSubscriptionService.updateTutor(id, childSubscriptionUpdateDTO);
        }
        if(StringUtils.isNotBlank(childSubscriptionUpdateDTO.getAdditionalInformation())) {
            childSubscriptionService.updateAdditionalInformation(id, childSubscriptionUpdateDTO);
        }
        if (StringUtils.isNotBlank(childSubscriptionUpdateDTO.getCardNumber())) {
            childSubscriptionService.updateCardNumber(id, childSubscriptionUpdateDTO);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /child-subscriptions : get all the childSubscriptions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of childSubscriptions in body
     */
    @GetMapping("/child-subscriptions")
    @Secured(AuthoritiesConstants.FAMILITY)
    public List<ChildSubscription> getAllChildSubscriptions() {
        log.debug("REST request to get all ChildSubscriptions");
        return childSubscriptionRepository.findAll();
    }

    /**
     * GET  /child-subscriptions/:id : get the "id" childSubscription.
     *
     * @param id the id of the childSubscription to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the childSubscriptionDetailDTO, or with status 404 (Not Found)
     */
    @GetMapping("/child-subscriptions/{id}")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<ChildSubscriptionDetailDTO> getChildSubscriptionDetailDTO(@PathVariable Long id) {
        log.debug("REST request to get Child Subscription: {}", id);
        accessValidator.canAccessChildSubscription(id);
        return ResponseEntity.ok().body(childSubscriptionService.findById(id));
    }

    /**
     * DELETE  /child-subscriptions/:id : delete the "id" childSubscription.
     *
     * @param id the id of the childSubscription to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/child-subscriptions/{id}")
    @Secured(AuthoritiesConstants.FAMILITY)
    public ResponseEntity<Void> deleteChildSubscription(@PathVariable Long id) {
        log.debug("REST request to delete ChildSubscription : {}", id);
        childSubscriptionRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code GET  /child-subscriptions/download} : downloads the children import template file
     *
     *@return the {@link ResponseEntity} with status {@code 200 (OK)} and with body InputStreamResource}.
     */
    @GetMapping("/child-subscriptions/download")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<InputStreamResource> download() throws IOException {
        InputStream bis = childSubscriptionService.download();
        return ResponseEntity
            .ok()
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(new InputStreamResource(bis));
    }

    /**
     * {@code POST  /child-subscriptions/upload} : uploads the children import file
     *
     * *@return the {@link ResponseEntity} with status {@code 200 (OK)} and with body childImport}.
     */
    @PostMapping("/child-subscriptions/upload")
    @Secured({AuthoritiesConstants.FAMILITY, AuthoritiesConstants.BUS_COMPANY})
    public ResponseEntity<ImportDTO> upload(@RequestParam("file") MultipartFile file) throws Exception {
        ImportDTO childrenImport = childSubscriptionService.upload(file.getInputStream());
        return ResponseEntity.ok().body(childrenImport);
    }
}
