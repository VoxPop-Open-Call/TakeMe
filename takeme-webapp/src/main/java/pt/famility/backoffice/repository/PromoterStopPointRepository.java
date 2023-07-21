package pt.famility.backoffice.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.famility.backoffice.domain.PromoterStopPoint;

/**
 * Spring Data JPA repository for the PromoterStopPoint entity.
 */
@Repository
public interface PromoterStopPointRepository extends JpaRepository<PromoterStopPoint, Long>, JpaSpecificationExecutor<PromoterStopPoint> {
    default Optional<PromoterStopPoint> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PromoterStopPoint> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PromoterStopPoint> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct promoterStopPoint from PromoterStopPoint promoterStopPoint left join fetch promoterStopPoint.promoterItinerary left join fetch promoterStopPoint.location",
        countQuery = "select count(distinct promoterStopPoint) from PromoterStopPoint promoterStopPoint"
    )
    Page<PromoterStopPoint> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct promoterStopPoint from PromoterStopPoint promoterStopPoint left join fetch promoterStopPoint.promoterItinerary left join fetch promoterStopPoint.location"
    )
    List<PromoterStopPoint> findAllWithToOneRelationships();

    @Query(
        "select promoterStopPoint from PromoterStopPoint promoterStopPoint left join fetch promoterStopPoint.promoterItinerary left join fetch promoterStopPoint.location where promoterStopPoint.id =:id"
    )
    Optional<PromoterStopPoint> findOneWithToOneRelationships(@Param("id") Long id);

    List<PromoterStopPoint> findByPromoterItineraryId(@Param("id") Long id);
}
