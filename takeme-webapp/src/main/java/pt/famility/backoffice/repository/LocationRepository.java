package pt.famility.backoffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.famility.backoffice.domain.Location;

/**
 * Spring Data JPA repository for the Location entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = "select location from Organization organization left join organization.serviceLocations as location where organization.id = :idOrganization and location.designation like :designation")
    Page<Location> findAllLocationsOfOrganization(@Param("idOrganization") Long idOrganization, @Param("designation") String designation, Pageable pageable);
}
