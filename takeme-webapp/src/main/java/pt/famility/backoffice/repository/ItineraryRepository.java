package pt.famility.backoffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.famility.backoffice.domain.Itinerary;
import pt.famility.backoffice.domain.enumeration.ItineraryStatusType;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Itinerary entity.
 */

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    List<Itinerary> findAllByDriverIdAndScheduledTimeBetween(Long driverId, Instant minToday, Instant maxToday);

    Page<Itinerary> findAllByItineraryStatusTypeInAndOrganization_IdAndNameIsContainingIgnoreCase(Collection<ItineraryStatusType> itineraryStatusType, Long organization_id, String name, Pageable pageable);

    Page<Itinerary> findAllByItineraryStatusTypeInAndNameIsContainingIgnoreCase(Collection<ItineraryStatusType> itineraryStatusType, String name, Pageable pageable);

    Optional<Itinerary> findFirstByDriverIdAndItineraryStatusTypeAndScheduledTimeBetween(Long driverId, ItineraryStatusType statusType, Instant minToday, Instant maxToday);
}
