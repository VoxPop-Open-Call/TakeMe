package pt.famility.backoffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.famility.backoffice.domain.ChildSubscription;
import pt.famility.backoffice.domain.enumeration.OrganizationType;
import pt.famility.backoffice.domain.enumeration.StatusType;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the ChildSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChildSubscriptionRepository extends JpaRepository<ChildSubscription, Long> {
    Page<ChildSubscription> findAllByOrganizationIdAndStatusType(Long organizationId, StatusType statusType, Pageable pageable);

    List<ChildSubscription> findAllByOrganizationIdAndStatusTypeIn(Long organizationId, List<StatusType> status);

    @Query(value = "select distinct cs.additionalInformation from ChildSubscription cs where cs.organization.id = :organizationId and cs.statusType = :status and cs.additionalInformation <> '' order by cs.additionalInformation asc")
    List<String> getAdditionalInformationEntries(@Param("organizationId") Long organizationId, @Param("status") StatusType status);

    Page<ChildSubscription> findAllByChild_IdAndOrganization_OrganizationTypeOrderByStatusTypeAsc(Long childId, OrganizationType organizationType, Pageable pageable);

    @Query(value = "select cs from ChildSubscription cs where cs.subscriptionDate in (select max(css.subscriptionDate) from ChildSubscription css where css.child.id = :childId and (css.organization.organizationType = :organizationType or :organizationType is null) and (css.statusType = :status or :status is null) group by css.organization.id) and cs.child.id = :childId")
    List<ChildSubscription> getSubscriptionsWithAuthorizationByChild(@Param("childId") Long childId, @Param("organizationType") OrganizationType organizationType, @Param("status") StatusType status);


    @Query(value = "select cs from ChildSubscription cs where cs.subscriptionDate in (select max(css.subscriptionDate) from ChildSubscription css where css.child.id = :childId and (css.organization.organizationType = :organizationType or :organizationType is null) and (css.statusType = :status or :status is null) group by css.organization.id) and cs.child.id = :childId",
        countQuery = "select count(cs) from ChildSubscription cs where cs.subscriptionDate in (select max(css.subscriptionDate) from ChildSubscription css where css.child.id = :childId and (css.organization.organizationType = :organizationType or :organizationType is null) and (css.statusType = :status or :status is null) group by css.organization.id) and cs.child.id = :childId")
    Page<ChildSubscription> listLastStateByChildIdAndOrganizations(@Param("childId") Long childId, @Param("organizationType") OrganizationType organizationType, @Param("status") StatusType status, Pageable pageable);

    List<ChildSubscription> findAllByChild_IdAndOrganization_OrganizationTypeOrderByStatusTypeAsc(Long childId, OrganizationType organizationType);

    @Query(value = "select cs from ChildSubscription cs where cs.child.id = :childId and cs.statusType = 'ACTIVE' group by cs.organization.organizationType, cs.id, cs.child.name order by cs.child.name asc")
    List<ChildSubscription> listActiveByChild(@Param("childId") Long childId);

    ChildSubscription findFirstByChild_IdAndOrganization_IdAndStatusType(Long childId, Long organizationId, StatusType statusType);

    List<ChildSubscription> findAllByChildIdAndStatusType(Long childId, StatusType statusType);

    List<ChildSubscription> findAllByChildId(Long childId);

    List<ChildSubscription> findAllByChildIdAndStatusTypeIn(Long childId, List<StatusType> statusTypes);

    List<ChildSubscription> findAllByStatusTypeAndChildIdAndOrganizationId(StatusType statusType, Long childId, Long organizationId);

    List<ChildSubscription> findAllByStatusTypeAndChildIdAndOrganizationIdAndUserId(StatusType statusType, Long childId, Long organizationId, Long userId);

    Optional<ChildSubscription> findFirstByStatusTypeAndChildIdAndOrganizationId(StatusType statusType, Long childId, Long organizationId);

    Optional<ChildSubscription> findByOrganizationIdAndChildNifCountryAndChildNifNumberAndChildFamilityAndStatusTypeAndFamility(Long organizationId, String nifCountry, String nif, Boolean childFamility, StatusType statusType, Boolean subscriptionFamility);

    Optional<ChildSubscription> findFirstByOrganizationIdAndChildNifNumberAndChildFamilityAndFamility(Long organizationId, String nif, Boolean childFamility, Boolean famility);

    List<ChildSubscription> findByOrganizationIdAndFamilityAndStatusType(Long organizationId, boolean famility, StatusType statusType);

    Optional<ChildSubscription> findByOrganizationIdAndChildNifCountryAndChildNifNumberAndStatusType(Long organizationId, String nifCountry, String nif, StatusType active);

    Optional<ChildSubscription> findFirstByOrganizationIdAndChildNifNumber(Long id, String childNif);

    Optional<ChildSubscription> findFirstByOrganizationIdAndCardNumberAndStatusType(Long organizationId, String cardNumber, StatusType statusType);

    Optional<ChildSubscription> findFirstByStatusTypeAndChildIdAndOrganizationIdAndFamilityFalse(StatusType statusType, Long childId, Long organizationId);

    ChildSubscription findByOrganizationIdAndChildIdAndStatusType(Long organizationId, Long childId, StatusType status);

}
