package pt.famility.backoffice.repository;

import pt.famility.backoffice.domain.UserNotification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserNotification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

}
