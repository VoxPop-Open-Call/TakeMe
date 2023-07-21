package pt.famility.backoffice.repository;

import pt.famility.backoffice.domain.ServiceStopPointDaysOfWeek;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ServiceStopPointDaysOfWeek entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceStopPointDaysOfWeekRepository extends JpaRepository<ServiceStopPointDaysOfWeek, Long> {

}
