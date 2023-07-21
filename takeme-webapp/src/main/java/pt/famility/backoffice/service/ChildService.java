package pt.famility.backoffice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.converter.ChildConverter;
import pt.famility.backoffice.domain.Child;
import pt.famility.backoffice.domain.ChildSubscription;
import pt.famility.backoffice.domain.DocumentFile;
import pt.famility.backoffice.domain.Organization;
import pt.famility.backoffice.domain.Tutor;
import pt.famility.backoffice.domain.enumeration.StatusType;
import pt.famility.backoffice.repository.ChildItinerarySubscriptionRepository;
import pt.famility.backoffice.repository.ChildRepository;
import pt.famility.backoffice.repository.ChildSubscriptionRepository;
import pt.famility.backoffice.repository.TutorRepository;
import pt.famility.backoffice.service.dto.ChildDTO;
import pt.famility.backoffice.service.dto.ChildItinerarySubscriptionDTO;
import pt.famility.backoffice.service.dto.ChildPatchDTO;
import pt.famility.backoffice.service.dto.PhotoDTO;
import pt.famility.backoffice.service.dto.TutorDTO;
import pt.famility.backoffice.service.exception.NifNumberAlreadyUsedException;
import pt.famility.backoffice.service.mapper.ChildItinerarySubscriptionMapper;
import pt.famility.backoffice.web.rest.errors.InvalidIDException;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Child}.
 */
@Service
@Transactional
public class ChildService {

    private final ChildRepository childRepository;

    private final TutorRepository tutorRepository;

    private final ChildConverter childConverter;

    private final PhotoService photoService;

    private final OrganizationService organizationService;

    private final TutorService tutorService;

    private final ChildSubscriptionRepository childSubscriptionRepository;

    private final ChildItinerarySubscriptionRepository childItinerarySubscriptionRepository;

    private final ChildItinerarySubscriptionMapper childItinerarySubscriptionMapper;

    public ChildService(
            ChildRepository childRepository,
            TutorRepository tutorRepository,
            ChildConverter childConverter,
            PhotoService photoService,
            OrganizationService organizationService,
            TutorService tutorService,
            ChildSubscriptionRepository childSubscriptionRepository,
            ChildItinerarySubscriptionRepository childItinerarySubscriptionRepository,
            ChildItinerarySubscriptionMapper childItinerarySubscriptionMapper
    ) {
        this.childRepository = childRepository;
        this.tutorRepository = tutorRepository;
        this.childConverter = childConverter;
        this.photoService = photoService;
        this.organizationService = organizationService;
        this.tutorService = tutorService;
        this.childSubscriptionRepository = childSubscriptionRepository;
        this.childItinerarySubscriptionRepository = childItinerarySubscriptionRepository;
        this.childItinerarySubscriptionMapper = childItinerarySubscriptionMapper;
    }

    public PhotoDTO getPhotoByChildAndPhotoFileId(Long id, Long photoFileId) {
        return childRepository
                .findByIdAndPhotoFileId(id, photoFileId)
                .map(child -> new PhotoDTO(Base64.getEncoder().encodeToString(child.getPhotoFile().getFileContent())))
                .orElseThrow(() -> new InvalidIDException("childPhoto"));
    }

    public ChildDTO save(Long tutorId, ChildDTO childDTO) throws IOException {
        return save(null, tutorId, childDTO);
    }

    public ChildDTO save(Long organizationId, Long tutorId, ChildDTO childDTO) throws IOException {

        if (childDTO.getFamility()) {
            List<Child> childList = childRepository.findByNifCountryAndNifNumberAndFamility(childDTO.getNifCountry(), childDTO.getNifNumber(), true);
            if (!childList.isEmpty()) {
                throw new NifNumberAlreadyUsedException();
            }
        } else {
            Optional<ChildSubscription> optionalChildSubscription = childSubscriptionRepository.findByOrganizationIdAndChildNifCountryAndChildNifNumberAndChildFamilityAndStatusTypeAndFamility(
                    organizationId, childDTO.getNifCountry(), childDTO.getNifNumber(), false, StatusType.ACTIVE, false);
            if (optionalChildSubscription.isPresent()) {
                throw new NifNumberAlreadyUsedException();
            }
        }

        Tutor tutor = tutorRepository.findOneByUserIdWithEagerRelationships(tutorId).orElseThrow(() -> new InvalidIDException("childRegister"));

        childDTO.setStatusType(StatusType.ACTIVE);
        if (tutor.isFamility()) {
            childDTO.setFamility(true);
        } else {
            childDTO.setFamility(false);
        }

        Child child = childConverter.convert(childDTO);

        if (childDTO.getPhoto() != null) {
            child.setPhotoFile(photoService.savePhoto(childDTO.getPhoto()));
        }

        child.getTutors().add(tutor);
        child = childRepository.save(child);
        tutor.getChildren().add(child);
        tutorRepository.save(tutor);
        childDTO.setId(child.getId());
        Optional.ofNullable(child.getPhotoFile()).ifPresent(documentFile -> childDTO.setPhotoId(documentFile.getId()));
        return childDTO;
    }

    public ChildDTO updatePhoto(Long childId, PhotoDTO photoDTO) throws IOException {
        Child child = childRepository.findById(childId).orElseThrow(() -> new InvalidIDException("childPhoto"));
        DocumentFile oldPhoto = child.getPhotoFile();
        DocumentFile documentFile = photoService.savePhoto(photoDTO.getPhoto());
        child.setPhotoFile(documentFile);
        childRepository.save(child);
        Optional.ofNullable(oldPhoto).ifPresent(documentFile1 -> photoService.deletePhoto(documentFile1));
        return childConverter.convert(child);
    }

    public Child findById(Long id) {
        return childRepository
                .findById(id)
                .orElseThrow(() -> new InvalidIDException("childService"));
    }

    public ChildDTO createChildByOrganization(Long id, Long idTutor, ChildDTO childDTO) throws Exception {

        Organization organization = organizationService.findById(id);
        Tutor tutor = tutorService.findById(idTutor).orElseThrow(() -> new InvalidIDException("tutorService"));

        childDTO.setFamility(false);
        childDTO.setStatusType(StatusType.ACTIVE);

        ChildDTO childSavedDTO = save(id, idTutor, childDTO);

        ChildSubscription childSubscription = new ChildSubscription()
                .subscriptionDate(Instant.now())
                .activationDate(Instant.now())
                .famility(false)
                .statusType(StatusType.ACTIVE)
                .organization(organization)
                .child(findById(childDTO.getId()))
                .user(tutor.getUser());

        childSubscriptionRepository.save(childSubscription);

        return childSavedDTO;
    }

    public void patchChild(Long id, ChildPatchDTO childPatchDTO) {
        Child child = findById(id);

        if (!Objects.equals(child.getNifNumber(), childPatchDTO.getNifNumber())) {
            List<Child> childList = childRepository.findByNifCountryAndNifNumberAndFamility(childPatchDTO.getNifCountry(), childPatchDTO.getNifNumber(), true);
            if (!childList.isEmpty()) {
                throw new NifNumberAlreadyUsedException();
            }
        }

        Optional.ofNullable(childPatchDTO.getName()).ifPresent(child::setName);
        Optional.ofNullable(childPatchDTO.getDateOfBirth()).ifPresent(dataOfBirth -> child.setDateOfBirth(LocalDate.parse(dataOfBirth)));
        Optional.ofNullable(childPatchDTO.getNifNumber()).ifPresent(child::setNifNumber);

        childRepository.save(child);
    }

    public List<TutorDTO> getTutorsByChildId(Long childId) {
        return childRepository
                .findById(childId)
                .orElseThrow(() -> new InvalidIDException("childService"))
                .getTutors()
                .stream()
                .map(tutorService::convert)
                .collect(Collectors.toList());
    }

    public Set<Tutor> getTutorsOfChild(Child child) {
        Child childLoad = findById(child.getId());
        return childLoad.getTutors();
    }

    public ChildDTO getChild(Long id) {
        return childConverter.convert(findById(id));
    }

    public List<ChildItinerarySubscriptionDTO> findItinerarySubscriptionsByChildId(Long id) {
        return childItinerarySubscriptionRepository
                .findByChildId(id)
                .map(childItinerarySubscriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    public Page<ChildDTO> getTutorChildrenByTutorId(Long tutorId, Pageable pageable) {
        return childRepository
                .findAllByTutorsId(tutorId, pageable)
                .map(childConverter::convert);
    }
}
