package pt.famility.backoffice.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.Child;
import pt.famility.backoffice.domain.ChildSubscription;
import pt.famility.backoffice.domain.enumeration.StatusType;
import pt.famility.backoffice.service.ChildService;
import pt.famility.backoffice.service.ContactService;
import pt.famility.backoffice.service.OrganizationService;
import pt.famility.backoffice.service.TutorService;
import pt.famility.backoffice.service.UserService;
import pt.famility.backoffice.service.dto.ChildSubscriptionChildViewDTO;
import pt.famility.backoffice.service.dto.ChildSubscriptionDTO;
import pt.famility.backoffice.service.dto.ChildSubscriptionDetailDTO;
import pt.famility.backoffice.web.rest.errors.InvalidIDException;

import java.time.Instant;
import java.util.Optional;

@Component
public class ChildSubscriptionConverter {

    private TutorService tutorService;

    private TutorConverter tutorConverter;

    private OrganizationConverter organizationConverter;

    private ChildConverter childConverter;

    private OrganizationService organizationService;

    private ChildService childService;

    private UserService userService;

    private ContactService contactService;

    private final ObjectMapper objectMapper;

    public ChildSubscriptionConverter(TutorService tutorService, TutorConverter tutorConverter, OrganizationConverter organizationConverter, ChildConverter childConverter, OrganizationService organizationService, ChildService childService, UserService userService, ContactService contactService, ObjectMapper objectMapper) {
        this.tutorService = tutorService;
        this.tutorConverter = tutorConverter;
        this.organizationConverter = organizationConverter;
        this.childConverter = childConverter;
        this.organizationService = organizationService;
        this.childService = childService;
        this.userService = userService;
        this.contactService = contactService;
        this.objectMapper = objectMapper;
    }

    public ChildSubscriptionDTO convert(ChildSubscription childSubscription) {

        ChildSubscriptionDTO childSubscriptionDTO = new ChildSubscriptionDTO();
        childSubscriptionDTO.setId(childSubscription.getId());
        childSubscriptionDTO.setChildName(childSubscription.getChild().getName());
        childSubscriptionDTO.setChildNif(childSubscription.getChild().getNifNumber());
        childSubscriptionDTO.setChildId(childSubscription.getChild().getId());
        childSubscriptionDTO.setChildDateOfBirth(childSubscription.getChild().getDateOfBirth());
        childSubscriptionDTO.setFamility(childSubscription.isFamility());
        Optional.ofNullable(childSubscription.getChild().getPhotoFile()).ifPresent(documentFile -> childSubscriptionDTO.setChildPhotoId(documentFile.getId()));

        childSubscriptionDTO.setAdditionalInformation(childSubscription.getAdditionalInformation());
        childSubscriptionDTO.setComments(childSubscription.getComments());
        childSubscriptionDTO.setSubscriptionDate(childSubscription.getSubscriptionDate());
        Optional.ofNullable(childSubscription.getActivationDate()).ifPresent(childSubscriptionDTO::setActivationDate);
        Optional.ofNullable(childSubscription.getDeactivationDate()).ifPresent(childSubscriptionDTO::setDeactivationDate);

        Long id = childSubscription.getUser().getId();

        if (tutorService.isTutor(id)) {
            if (tutorService.getAllByUserId(id).size() == 1) {
                tutorService.getByUserId(id).ifPresent(tutor -> {
                        setUserInfo(childSubscriptionDTO, tutor.getName(), tutor.getNifNumber(), tutor.getId());
                        Optional.ofNullable(tutor.getPhotoFile()).ifPresent(documentFile -> childSubscriptionDTO.setUserPhotoId(documentFile.getId()));
                    }
                );
            } else {
                Child child = childSubscription.getChild();
                childService.getTutorsOfChild(child).stream().findFirst().ifPresent(tutor -> {
                    setUserInfo(childSubscriptionDTO, tutor.getName(), tutor.getNifNumber(), tutor.getId());
                    Optional.ofNullable(tutor.getPhotoFile()).ifPresent(documentFile -> childSubscriptionDTO.setUserPhotoId(documentFile.getId()));
                });
            }

        } else {
            organizationService.getByUserId(id).ifPresent(organization -> {
                    setUserInfo(childSubscriptionDTO, organization.getName(), organization.getNifNumber(), organization.getId());
                    Optional.ofNullable(organization.getPhotoFile()).ifPresent(documentFile -> childSubscriptionDTO.setUserPhotoId(documentFile.getId()));
                }

            );
        }

        return childSubscriptionDTO;
    }

    public ChildSubscriptionChildViewDTO convertChildView(ChildSubscription childSubscription) {
        ChildSubscriptionChildViewDTO childSubscriptionChildViewDTO = new ChildSubscriptionChildViewDTO();

        childSubscriptionChildViewDTO.setId(childSubscription.getId());
        childSubscriptionChildViewDTO.setComments(childSubscription.getComments());
        childSubscriptionChildViewDTO.setStatusType(childSubscription.getStatusType());
        childSubscriptionChildViewDTO.setOrganization(organizationService.convert(childSubscription.getOrganization()));
        childSubscriptionChildViewDTO.setChildId(childSubscription.getChild().getId());
        childSubscriptionChildViewDTO.setOrganizationContacts(contactService.getOrganizationPublicContactsByLocationId(childSubscription.getOrganization().getId()));

        return childSubscriptionChildViewDTO;
    }

    private void setUserInfo(ChildSubscriptionDTO childSubscriptionDTO, String userName, String userNif, Long userId) {
        childSubscriptionDTO.setUserName(userName);
        childSubscriptionDTO.setUserNif(userNif);
        childSubscriptionDTO.setUserId(userId);
    }

    public ChildSubscriptionDetailDTO convertDetail(ChildSubscription childSubscription) {

        ChildSubscriptionDetailDTO childSubscriptionDetailDTO = objectMapper.convertValue(childSubscription, ChildSubscriptionDetailDTO.class);

        Long id = childSubscription.getUser().getId();

        if (tutorService.isTutor(id)) {
            if (tutorService.getAllByUserId(id).size() == 1) {
                tutorService.getByUserId(id).ifPresent(tutor ->
                    childSubscriptionDetailDTO.setTutor(tutorConverter.convert(tutor))
                );
            } else {
                Child child = childSubscription.getChild();
                childService.getTutorsOfChild(child).stream().findFirst().ifPresent(tutor -> childSubscriptionDetailDTO.setTutor(tutorConverter.convert(tutor)));
            }

        } else {
            organizationService.getByUserId(id).ifPresent(organization ->
                childSubscriptionDetailDTO.setOrganization(organizationConverter.convert(organization))
            );
        }

        childSubscriptionDetailDTO.setChild(childConverter.convert(childSubscription.getChild()));

        return childSubscriptionDetailDTO;
    }

    public ChildSubscription convertRegister(Long childId, Long organizationId, Long userId) {

        return new ChildSubscription()
            .subscriptionDate(Instant.now())
            .famility(tutorService.isTutor(userId))
            .statusType(StatusType.PENDING)
            .organization(organizationService.findById(organizationId))
            .child(childService.findById(childId))
            .user(userService.loadUserById(userId).orElseThrow(() -> new InvalidIDException("childSubscriptionConverter")));
    }

}
