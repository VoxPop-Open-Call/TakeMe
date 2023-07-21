package pt.famility.backoffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pt.famility.backoffice.domain.Driver;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Driver entity.
 */

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByUserId_Id(Long userId);

    Optional<Driver> findByIdAndPhotoFileId(Long id, Long photoFileId);

    Page<Driver> findAllByOrganization_IdAndNameContainingAndUserIdIsNull(Long organizationId, String name, Pageable pageable);
}
