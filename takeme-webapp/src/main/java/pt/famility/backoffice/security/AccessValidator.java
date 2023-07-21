package pt.famility.backoffice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.domain.Location;
import pt.famility.backoffice.domain.enumeration.LocationType;
import pt.famility.backoffice.service.ChildService;
import pt.famility.backoffice.service.ChildSubscriptionService;
import pt.famility.backoffice.service.DriverService;
import pt.famility.backoffice.service.ItineraryService;
import pt.famility.backoffice.service.LocationService;
import pt.famility.backoffice.service.OrganizationService;
import pt.famility.backoffice.service.ServiceService;
import pt.famility.backoffice.service.TutorService;
import pt.famility.backoffice.service.UserService;
import pt.famility.backoffice.service.VehicleService;
import pt.famility.backoffice.service.dto.ItineraryDTO;
import pt.famility.backoffice.service.dto.TutorDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AccessValidator {

    private final String UNAUTHORIZED_ACTION = "Unauthorized action for this user.";

    @Autowired
    private UserService userService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ChildService childService;

    @Autowired
    private ChildSubscriptionService childSubscriptionService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ServiceService serviceService;

    // GENERIC

    private Set<String> getUserAuthorities() {
        return this.userService.getUserInfo().getAuthorities();
    }

    private boolean noFamilityRoleUser() {
        return !getUserAuthorities().contains(AuthoritiesConstants.FAMILITY);
    }

    private void isSessionUserId(Long userId) {
        Long sessionUserId = userService.getUserInfo().getId();
        if (!sessionUserId.equals(userId)) {
            log.warn("Session user ID, " + sessionUserId +", does not match provided user ID, " + userId + ".");
            throw new AccessDeniedException(UNAUTHORIZED_ACTION);
        }
    }

    private void isSessionOrganizationId(Long organizationId) {
        Long sessionOrganizationId = userService.getUserInfo().getOrganizationId();
        if (!sessionOrganizationId.equals(organizationId)) {
            log.warn("Session organization ID, " + sessionOrganizationId + ", does not match provided organization ID, " + organizationId + ".");
            throw new AccessDeniedException(UNAUTHORIZED_ACTION);
        }
    }

    private void isSessionTutorId(Long tutorId) {
        Long sessionTutorId = userService.getUserInfo().getTutor().getId();
        if (!sessionTutorId.equals(tutorId)) {
            log.warn("Session tutor ID, " + sessionTutorId + ", does not match provided tutor ID, " + tutorId + ".");
            throw new AccessDeniedException(UNAUTHORIZED_ACTION);
        }
    }

    private void isSessionDriverId(Long driverId) {
        Long sessionDriverId = userService.getUserInfo().getDriverId();
        if (!sessionDriverId.equals(driverId)) {
            log.warn("Session driver ID, " + sessionDriverId + ", does not match provided driver ID, " + driverId + ".");
            throw new AccessDeniedException(UNAUTHORIZED_ACTION);
        }
    }

    private void hasChildAuthorization(Long childId) {
        boolean isChildTutor = true;
        List<Long> childTutorUserIds = childService.getTutorsByChildId(childId).stream().map(TutorDTO::getUserId).collect(Collectors.toList());
        Long sessionUserId = this.userService.getUserInfo().getId();
        if (!childTutorUserIds.contains(sessionUserId)) {
            isChildTutor = false;
        }
        if (!isChildTutor) {
            log.warn("Session Tutor, with User ID "  + sessionUserId
                    + ", is not the child's tutor. Provided child's id is "
                    + childId);
            throw new AccessDeniedException(UNAUTHORIZED_ACTION);
        }
    }

    // CHILDREN

    public void canAccessChildPhoto(Long childId) {
        if (noFamilityRoleUser()) {
            Set<String> userAuthorities = getUserAuthorities();
            boolean organizationMismatch = false;
            if (userAuthorities.contains(AuthoritiesConstants.TUTOR)) {
                hasChildAuthorization(childId);
            } else if (userAuthorities.contains(AuthoritiesConstants.BUS_COMPANY)
                    && !childSubscriptionService.getOrganizationIdsWithSubscriptionsByChildId(childId).contains(this.userService.getUserInfo().getOrganizationId())) {
                organizationMismatch = true;
            } else if (((userAuthorities.contains(AuthoritiesConstants.BUS_COMPANY_DRIVER)) && !userAuthorities.contains(AuthoritiesConstants.BUS_COMPANY))
                    && !childSubscriptionService.getOrganizationIdsWithActiveSubscriptionsByChildId(childId).contains(this.userService.getUserInfo().getOrganizationId())) {
                organizationMismatch = true;
            }
            if (organizationMismatch) {
                log.warn("Session organization ID does not match any of the organizations of the child's active subscriptions.");
                throw new AccessDeniedException(UNAUTHORIZED_ACTION);
            }
        }
    }

    public void canPatchChild(Long childId) {
        Set<String> authorities = getUserAuthorities();
        if (authorities.contains(AuthoritiesConstants.BUS_COMPANY)
                && !childSubscriptionService.getOrganizationIdsWithActiveSubscriptionsByChildId(childId).contains(this.userService.getUserInfo().getOrganizationId())) {
            log.warn("Session organization ID does not match any of the organizations of the child's active subscriptions.");
            throw new AccessDeniedException(UNAUTHORIZED_ACTION);
        }
    }

    public void canPatchChildPhoto(Long childId) {
        Set<String> authorities = getUserAuthorities();
        if (authorities.contains(AuthoritiesConstants.TUTOR)) {
            List<Long> childTutorUserIds = childService.getTutorsByChildId(childId).stream().map(TutorDTO::getUserId).collect(Collectors.toList());
            if (!childTutorUserIds.contains(this.userService.getUserInfo().getId())) {
                log.warn("Session User ID does not match any of the Child Tutors User IDs.");
                throw new AccessDeniedException(UNAUTHORIZED_ACTION);
            }
        }
        if (authorities.contains(AuthoritiesConstants.BUS_COMPANY)
                && !childSubscriptionService.getOrganizationIdsWithActiveSubscriptionsByChildId(childId).contains(this.userService.getUserInfo().getOrganizationId())) {
            log.warn("Session organization ID does not match any of the organizations of the child's active subscriptions.");
            throw new AccessDeniedException(UNAUTHORIZED_ACTION);
        }
    }

    public void canAccessChildTutors(Long childId) {
        if (noFamilityRoleUser()) {
            List<Long> organizationIdsWithActiveSubscriptionsByChildId = childSubscriptionService.getOrganizationIdsWithActiveSubscriptionsByChildId(childId);
            if (!organizationIdsWithActiveSubscriptionsByChildId.contains(this.userService.getUserInfo().getOrganizationId())) {
                log.warn("Session organization ID does not match any of the organizations of the child's active subscriptions.");
                throw new AccessDeniedException(UNAUTHORIZED_ACTION);
            }
        }
    }

    public void canAccessChildSubscriptions(Long childId) {
        Set<String> authorities = getUserAuthorities();
        if (authorities.contains(AuthoritiesConstants.TUTOR)) {
            List<Long> childTutorUserIds = childService.getTutorsByChildId(childId).stream().map(TutorDTO::getUserId).collect(Collectors.toList());
            if (!childTutorUserIds.contains(this.userService.getUserInfo().getId())) {
                log.warn("Session User ID does not match any of the Child Tutors User IDs.");
                throw new AccessDeniedException(UNAUTHORIZED_ACTION);
            }
        }
        if (authorities.contains(AuthoritiesConstants.BUS_COMPANY)) {
            List<Long> organizationIdsWithActiveSubscriptionsByChildId = childSubscriptionService.getOrganizationIdsWithActiveSubscriptionsByChildId(childId);
            if (!organizationIdsWithActiveSubscriptionsByChildId.contains(this.userService.getUserInfo().getOrganizationId())) {
                log.warn("Session organization ID does not match any of the organizations of the child's active subscriptions.");
                throw new AccessDeniedException(UNAUTHORIZED_ACTION);
            }
        }
    }

    // CHILD-SUBSCRIPTIONS

    public void canPatchChildSubscription(Long childSubscriptionId) {
        if (noFamilityRoleUser() && !childSubscriptionService.getOrganizationIdByChildSubscriptionId(childSubscriptionId).equals(this.userService.getUserInfo().getOrganizationId())) {
            log.warn("Session organization ID does not match the organization ID of the child subscription.");
            throw new AccessDeniedException(UNAUTHORIZED_ACTION);
        }
    }

    public void canAccessChildSubscription(Long childSubscriptionId) {
        if (noFamilityRoleUser() && !childSubscriptionService.getOrganizationIdByChildSubscriptionId(childSubscriptionId).equals(this.userService.getUserInfo().getOrganizationId())) {
            log.warn("Session organization ID does not match the organization ID of the child subscription.");
            throw new AccessDeniedException(UNAUTHORIZED_ACTION);
        }
    }

    public void canCreateChildSubscription(Long childId) {
        if (getUserAuthorities().contains(AuthoritiesConstants.TUTOR)) {
            List<Long> childTutorUserIds = childService.getTutorsByChildId(childId).stream().map(TutorDTO::getUserId).collect(Collectors.toList());
            if (!childTutorUserIds.contains(this.userService.getUserInfo().getId())) {
                log.warn("Session User ID does not match any of the Child Tutors User IDs.");
                throw new AccessDeniedException(UNAUTHORIZED_ACTION);
            }
        }
    }

    // DRIVERS

    public void canCreateDriver(Long organizationId) {
        if (getUserAuthorities().contains(AuthoritiesConstants.BUS_COMPANY)) {
            isSessionOrganizationId(organizationId);
        }
    }

    public void canAccessDriverPhoto(Long driverId) {
        this.busCompanyAdminCanPerformDriverAction(driverId);
    }

    public void canAccessDriver(Long driverId) {
        this.busCompanyAdminCanPerformDriverAction(driverId);
    }

    public void canUpdateDriver(Long driverId) {
        this.busCompanyAdminCanPerformDriverAction(driverId);
    }

    private void busCompanyAdminCanPerformDriverAction(Long driverId) {
        if (getUserAuthorities().contains(AuthoritiesConstants.BUS_COMPANY)) {
            isSessionOrganizationId(driverService.findById(driverId).getOrganization().getId());
        }
    }

    public void canAccessItineraries(Long driverId) {
        if (getUserAuthorities().contains(AuthoritiesConstants.BUS_COMPANY_DRIVER)) {
            isSessionDriverId(driverId);
        }
    }

    public void canAccessCurrentItinerary(Long driverId) {
        if (getUserAuthorities().contains(AuthoritiesConstants.BUS_COMPANY_DRIVER)) {
            isSessionDriverId(driverId);
        }
    }

    // ITINERARIES

    public void canUpdateItinerary(ItineraryDTO itineraryDTO) {
        Set<String> authorities = getUserAuthorities();
        if (authorities.contains(AuthoritiesConstants.BUS_COMPANY_DRIVER)) {
            isSessionDriverId(itineraryDTO.getDriver().getId());
        }
        if (authorities.contains(AuthoritiesConstants.BUS_COMPANY)) {
            isSessionOrganizationId(itineraryDTO.getOrganization().getId());
        }
    }

    public void canDeleteItinerary(Long itineraryId) {
        if (noFamilityRoleUser()) {
            isSessionOrganizationId(itineraryService.findItineraryById(itineraryId).getOrganization().getId());
        }
    }

    public void canCreateItinerary(Long organizationId) {
        if (noFamilityRoleUser()) {
            isSessionOrganizationId(organizationId);
        }
    }

    public void canAccessItineraryAndUpdateItineraryStatus(Long itineraryId) {
        Set<String> authorities = getUserAuthorities();
        if (authorities.contains(AuthoritiesConstants.BUS_COMPANY_DRIVER)) {
            isSessionDriverId(itineraryService.findItineraryById(itineraryId).getDriver().getId());
        }
        if (authorities.contains(AuthoritiesConstants.BUS_COMPANY)) {
            isSessionOrganizationId(itineraryService.findItineraryById(itineraryId).getOrganization().getId());
        }
    }

    public void canFinishItineraryStopPoint(Long itineraryId) {
        if (getUserAuthorities().contains(AuthoritiesConstants.BUS_COMPANY_DRIVER)) {
            isSessionDriverId(itineraryService.findItineraryById(itineraryId).getDriver().getId());
        }
    }

    // LOCATIONS

    public void canAccessLocation(Long locationId) {
        if (noFamilityRoleUser()) {
            if (!locationService.findById(locationId).getType().equals(LocationType.SCHOOL)) {
                if (!organizationService.getServiceLocationIdsByOrganizationId(this.userService.getUserInfo().getOrganizationId()).contains(locationId)) {
                    log.warn("Provided Location ID does not match any service Location IDs associated with the session's Organization.");
                    throw new AccessDeniedException(UNAUTHORIZED_ACTION);
                }
            }
        }
    }

    public void canDeleteOrUpdateLocation(Long locationId) {
        if (noFamilityRoleUser()) {
            if (!organizationService.getServiceLocationIdsByOrganizationId(this.userService.getUserInfo().getOrganizationId()).contains(locationId)) {
                log.warn("Provided Location ID does not match any service Location IDs associated with the session's Organization.");
                throw new AccessDeniedException(UNAUTHORIZED_ACTION);
            }
        }
    }

    // ORGANIZATIONS

    public void canAccessOrganization(Long organizationId) {
        Set<String> authorities = getUserAuthorities();

        /*if (authorities.contains(AuthoritiesConstants.TUTOR)) {
            Long sessionUserId = this.userService.getUserInfo().getId();
            if (!childSubscriptionService.getUserIdsWithActiveSubscriptionsToOrganizationByOrganizationId(organizationId).contains(sessionUserId)) {
                log.warn("Provided Organization ID, " + organizationId + ", does not match any ACTIVE Child Subscriptions matching the session's User ID, " + sessionUserId + ".");
                throw new AccessDeniedException(UNAUTHORIZED_ACTION);
            }
        }*/
        if (authorities.contains(AuthoritiesConstants.BUS_COMPANY) ||
            authorities.contains(AuthoritiesConstants.TUTOR)) {
            isSessionOrganizationId(organizationId);
        }
    }

    public void canAccessOrganizationsWithoutSubscription(Long childId) {
        if (noFamilityRoleUser()) {
            List<Long> childTutorIds = childService.getTutorsByChildId(childId).stream().map(TutorDTO::getId).collect(Collectors.toList());
            if (!childTutorIds.contains(this.userService.getUserInfo().getTutor().getId())) {
                log.warn("Session Tutor ID does not match any of the Child's Tutor IDs.");
                throw new AccessDeniedException(UNAUTHORIZED_ACTION);
            }
        }
    }

    public void canPerformOrganizationRelatedAction(Long organizationId) {
        if (noFamilityRoleUser()) {
            isSessionOrganizationId(organizationId);
        }
    }

    @Transactional
    public void canDeleteOrganizationServiceLocation(Long organizationId, Long locationId) {
        if (noFamilityRoleUser()) {
            isSessionOrganizationId(organizationId);
            List<Long> servicelocationIds = organizationService.findById(organizationId).getServiceLocations().stream().map(Location::getId).collect(Collectors.toList());
            if (!servicelocationIds.contains(locationId)) {
                log.warn("Provided Location does not match any service Location for the provided Organization.");
                throw new AccessDeniedException(UNAUTHORIZED_ACTION);
            }
        }
    }

    // SERVICES

    public void canAccessOrDeleteService(Long serviceId) {
        if (noFamilityRoleUser()) {
            isSessionOrganizationId(serviceService.getServiceDetail(serviceId).getOrganization().getId());
        }
    }

    public void canCreateOrUpdateService(Long organizationId) {
        if (noFamilityRoleUser()) {
            isSessionOrganizationId(organizationId);
        }
    }

    // TUTORS

    public void userCanCreateTutor(Long userId) {
        if (!this.tutorService.getAllByUserId(userId).isEmpty()) {
            log.warn("Session User ID matches a Tutor.");
            throw new AccessDeniedException(UNAUTHORIZED_ACTION);
        }
        if (noFamilityRoleUser()) {
            isSessionUserId(userId);
        }
    }

    public void canPerformTutorAction(Long tutorId) {
        if (noFamilityRoleUser()) {
            isSessionTutorId(tutorId);
        }
    }

    public void canAccessTutorPhoto(Long tutorId) {
        if (getUserAuthorities().contains(AuthoritiesConstants.TUTOR)) {
            isSessionTutorId(tutorId);
        }
    }

    public void canPatchTutorPhoto(Long tutorId) {
        if (getUserAuthorities().contains(AuthoritiesConstants.BUS_COMPANY)) {
            isSessionOrganizationId(tutorService.getOrganizationIdByTutorId(tutorId));
        }
    }

    public void canAccessOrganizationTutors(Long organizationId) {
        Set<String> authorities = getUserAuthorities();
        if (authorities.contains(AuthoritiesConstants.BUS_COMPANY)) {
            isSessionOrganizationId(organizationId);
        }
    }

    public void canAccessTutor(Long tutorId) {
        Set<String> authorities = getUserAuthorities();
        if (authorities.contains(AuthoritiesConstants.BUS_COMPANY)) {
            isSessionOrganizationId(tutorService.getOrganizationIdByTutorId(tutorId));
        }
    }

    public void canPatchTutor(Long tutorId) {
        if (noFamilityRoleUser()) {
            Set<String> authorities = this.userService.getUserInfo().getAuthorities();
            if (authorities.contains(AuthoritiesConstants.TUTOR)) {
                canPerformTutorAction(tutorId);
            }
            if (authorities.contains(AuthoritiesConstants.BUS_COMPANY)) {
                isSessionOrganizationId(tutorService.getOrganizationIdByTutorId(tutorId));
            }
        }
    }

    // USER

    public void canPerformUserAction(Long userId) {
        if (noFamilityRoleUser()) {
            isSessionOrganizationId(organizationService.getOrganizationIdByUserId(userId));
        }
    }

    public void canGetUsers(Long organizationId) {
        if (noFamilityRoleUser()) {
            isSessionOrganizationId(organizationId);
        }
    }

    // VEHICLES

    public void canPerformVehicleAction(Long vehicleId) {
        if (noFamilityRoleUser()) {
            isSessionOrganizationId(vehicleService.findById(vehicleId).getOrganization().getId());
        }
    }

    public void canCreateVehicle(Long organizationId) {
        if (noFamilityRoleUser()) {
            isSessionOrganizationId(organizationId);
        }
    }

    // USER MESSAGING TOKENS

    public void canCreateUserMessagingToken(Long userId) {
        if (noFamilityRoleUser()) {
           isSessionUserId(userId);
        }
    }

}
