package pt.famility.backoffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.famility.backoffice.domain.Service;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data JPA repository for the Service entity.
 */
@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findAllByOrganizationIdAndChildItinerarySubscriptionIdAndEndDateIsAfterOrOrganizationIdAndChildItinerarySubscriptionIdAndEndDateIsNull(Long idOrganization, Long idChildItinerarySubscription, Instant endDate, Long idOrganization1, Long idChildItinerarySubscription1);

    Page<Service> findAllByOrganizationIdAndChildItinerarySubscriptionIdAndEndDateIsAfterOrOrganizationIdAndChildItinerarySubscriptionIdAndEndDateIsNull(Long idOrganization, Long idChildItinerarySubscription, Instant endDate, Long idOrganization1, Long idChildItinerarySubscription1, Pageable pageable);

    Page<Service> findAllByChildItinerarySubscriptionId(Long subscriptionId, Pageable pageable);

    List<Service> findAllByChildItinerarySubscriptionPromoterItineraryIdAndChildItinerarySubscriptionIdAndEndDateIsAfterOrChildItinerarySubscriptionPromoterItineraryIdAndChildItinerarySubscriptionIdAndEndDateIsNull(Long childItinerarySubscription_promoterItinerary_id, Long childItinerarySubscription_id, Instant endDate, Long childItinerarySubscription_promoterItinerary_id2, Long childItinerarySubscription_id2);
}
