package pt.famility.backoffice.repository;

import pt.famility.backoffice.domain.StopAuditEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StopAuditEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StopAuditEventRepository extends JpaRepository<StopAuditEvent, Long> {

}
