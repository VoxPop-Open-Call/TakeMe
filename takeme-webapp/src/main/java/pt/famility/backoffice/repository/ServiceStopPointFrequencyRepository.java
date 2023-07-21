package pt.famility.backoffice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.famility.backoffice.domain.ServiceStopPointFrequency;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServiceStopPointFrequency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceStopPointFrequencyRepository extends JpaRepository<ServiceStopPointFrequency, Long> {
}
