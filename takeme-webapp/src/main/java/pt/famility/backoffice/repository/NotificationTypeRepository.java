package pt.famility.backoffice.repository;

import pt.famility.backoffice.domain.NotificationType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pt.famility.backoffice.domain.enumeration.NotificationTypeType;

import java.util.Optional;


/**
 * Spring Data  repository for the NotificationType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {

    Optional<NotificationType> findByType(NotificationTypeType type);
}
