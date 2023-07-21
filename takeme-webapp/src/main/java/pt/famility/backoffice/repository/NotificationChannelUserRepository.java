package pt.famility.backoffice.repository;

import pt.famility.backoffice.domain.NotificationChannelUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the NotificationChannelUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationChannelUserRepository extends JpaRepository<NotificationChannelUser, Long> {

}
