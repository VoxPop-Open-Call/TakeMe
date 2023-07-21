package pt.famility.backoffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.famility.backoffice.domain.ChildItinerarySubscription;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Spring Data JPA repository for the ChildItinerarySubscription entity.
 */
@Repository
public interface ChildItinerarySubscriptionRepository
        extends JpaRepository<ChildItinerarySubscription, Long>, JpaSpecificationExecutor<ChildItinerarySubscription> {
    @Query("select childItinerarySubscription from ChildItinerarySubscription childItinerarySubscription where childItinerarySubscription.user.login = ?#{principal.username}")
    List<ChildItinerarySubscription> findByUserIsCurrentUser();

    default Optional<ChildItinerarySubscription> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ChildItinerarySubscription> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ChildItinerarySubscription> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
            value = "select distinct childItinerarySubscription from ChildItinerarySubscription childItinerarySubscription left join fetch childItinerarySubscription.child left join fetch childItinerarySubscription.promoterItinerary left join fetch childItinerarySubscription.user",
            countQuery = "select count(distinct childItinerarySubscription) from ChildItinerarySubscription childItinerarySubscription"
    )
    Page<ChildItinerarySubscription> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct childItinerarySubscription from ChildItinerarySubscription childItinerarySubscription left join fetch childItinerarySubscription.child left join fetch childItinerarySubscription.promoterItinerary left join fetch childItinerarySubscription.user")
    List<ChildItinerarySubscription> findAllWithToOneRelationships();

    @Query("select childItinerarySubscription from ChildItinerarySubscription childItinerarySubscription left join fetch childItinerarySubscription.child left join fetch childItinerarySubscription.promoterItinerary left join fetch childItinerarySubscription.user where childItinerarySubscription.id =:id")
    Optional<ChildItinerarySubscription> findOneWithToOneRelationships(@Param("id") Long id);

    Stream<ChildItinerarySubscription> findByChildId(@Param("id") Long id);

    void deleteByPromoterItineraryId(@Param("id") Long id);

    List<ChildItinerarySubscription> findAllByPromoterItineraryId(Long promoterItinerary_id);
}
