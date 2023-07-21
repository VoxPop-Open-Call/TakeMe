package pt.famility.backoffice.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pt.famility.backoffice.domain.PromoterService;

/**
 * Spring Data JPA repository for the PromoterService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PromoterServiceRepository extends JpaRepository<PromoterService, Long>, JpaSpecificationExecutor<PromoterService> {}
