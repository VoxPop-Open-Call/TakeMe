package pt.famility.backoffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.famility.backoffice.domain.ItineraryStopPoint;
import pt.famility.backoffice.domain.enumeration.ItineraryStatusType;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ItineraryStopPoint entity.
 */

@Repository
public interface ItineraryStopPointRepository extends JpaRepository<ItineraryStopPoint, Long> {

    @Query(value = "select isp from ItineraryStopPoint isp where isp.itinerary.organization.id = :organizationId and isp.stopPoint.id in (select sp.id from StopPoint sp join sp.childList c on c.id = :childId where sp.scheduledTime > :scheduledTime) order by isp.order asc",
        countQuery = "select count(isp) from ItineraryStopPoint isp where isp.itinerary.organization.id = :organizationId and isp.stopPoint.id in (select sp.id from StopPoint sp join sp.childList c on c.id = :childId where sp.scheduledTime > :scheduledTime)")
    Page<ItineraryStopPoint> listAllItineraryStopPoint(@Param("childId") Long childId, @Param("organizationId") Long organizationId, @Param("scheduledTime") Instant scheduledTime, Pageable pageable);

    @EntityGraph(attributePaths = "stopPoint.childList")
    List<ItineraryStopPoint> findAllByItinerary_Id(Long itineraryId);

    List<ItineraryStopPoint> findAllByItinerary_IdAndItineraryStatusType(Long itineraryId, ItineraryStatusType statusType);

    List<ItineraryStopPoint> findAllByItinerary_IdAndOrderGreaterThanOrderByOrder(Long itineraryId, Integer order);

    Optional<ItineraryStopPoint> findFirstByItinerary_IdAndItineraryStatusType(Long itineraryId, ItineraryStatusType statusType);

    Optional<ItineraryStopPoint> findFirstByItinerary_IdAndItineraryStatusTypeOrderByOrder(Long itineraryId, ItineraryStatusType statusType);

    Optional<ItineraryStopPoint> findFirstByItinerary_IdAndItineraryStatusTypeOrderByOrderDesc(Long itineraryId, ItineraryStatusType statusType);

}
