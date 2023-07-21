package pt.famility.backoffice.repository;

import pt.famility.backoffice.domain.StopPoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StopPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StopPointRepository extends JpaRepository<StopPoint, Long> {

}
