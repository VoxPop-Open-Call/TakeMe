package pt.famility.backoffice.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.famility.backoffice.domain.PromoterItinerary;

/**
 * Spring Data JPA repository for the PromoterItinerary entity.
 */
@Repository
public interface PromoterItineraryRepository extends JpaRepository<PromoterItinerary, Long>, JpaSpecificationExecutor<PromoterItinerary> {
    default Optional<PromoterItinerary> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PromoterItinerary> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PromoterItinerary> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct promoterItinerary from PromoterItinerary promoterItinerary left join fetch promoterItinerary.service",
        countQuery = "select count(distinct promoterItinerary) from PromoterItinerary promoterItinerary"
    )
    Page<PromoterItinerary> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct promoterItinerary from PromoterItinerary promoterItinerary left join fetch promoterItinerary.service")
    List<PromoterItinerary> findAllWithToOneRelationships();

    @Query(
        "select promoterItinerary from PromoterItinerary promoterItinerary left join fetch promoterItinerary.service where promoterItinerary.id =:id"
    )
    Optional<PromoterItinerary> findOneWithToOneRelationships(@Param("id") Long id);

    Stream<PromoterItinerary> findByServiceId(@Param("id") Long id);

    Page<PromoterItinerary> findByOrganizationId(@Param("id") Long id, Pageable pageable);

    Stream<PromoterItinerary> findByServiceIdAndEndDateNull(@Param("id") Long id);
}
