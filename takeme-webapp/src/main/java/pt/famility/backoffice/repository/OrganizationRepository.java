package pt.famility.backoffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.famility.backoffice.domain.Organization;
import pt.famility.backoffice.domain.enumeration.OrganizationType;
import pt.famility.backoffice.domain.enumeration.StatusType;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Organization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @Query(value = "select distinct organization from Organization organization " +
        " left join fetch organization.contacts",
        countQuery = "select count(distinct organization) from Organization organization")
    Page<Organization> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct organization from Organization organization " +
        " left join fetch organization.contacts")
    List<Organization> findAllWithEagerRelationships();

    @Query("select distinct organization from Organization organization " +
        " left join fetch organization.contacts where organization.id = :id")
    Optional<Organization> findOneWithEagerRelationships(@Param("id") Long id);

    Page<Organization> findAllByStatusTypeAndOrganizationTypeAndIdNotIn(StatusType statusType, OrganizationType organizationType, List<Long> ids, Pageable pageable);

    Page<Organization> findAllByStatusTypeAndOrganizationType(StatusType statusType, OrganizationType organizationType, Pageable pageable);

    Optional<Organization> findByIdAndPhotoFileId(Long id, Long photoFileId);

    Optional<Organization> findByLocationId(Long locationId);

}
