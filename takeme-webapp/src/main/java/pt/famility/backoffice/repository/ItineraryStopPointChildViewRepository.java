package pt.famility.backoffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.famility.backoffice.domain.ItineraryStopPointChildView;

import java.util.List;


/**
 * Spring Data  repository for the ItineraryStopPointChildView entity.
 */
@Repository
public interface ItineraryStopPointChildViewRepository extends JpaRepository<ItineraryStopPointChildView, Long> {

    Page<ItineraryStopPointChildView> findByChildIdIn(List<Long> childId, Pageable pageable);
}
