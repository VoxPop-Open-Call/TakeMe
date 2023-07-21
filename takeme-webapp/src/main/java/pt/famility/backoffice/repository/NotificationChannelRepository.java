package pt.famility.backoffice.repository;

import pt.famility.backoffice.domain.NotificationChannel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pt.famility.backoffice.domain.enumeration.NotificationChannelType;

import java.util.Optional;


/**
 * Spring Data  repository for the NotificationChannel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationChannelRepository extends JpaRepository<NotificationChannel, Long> {

    Optional<NotificationChannel> findByType(NotificationChannelType type);
}
