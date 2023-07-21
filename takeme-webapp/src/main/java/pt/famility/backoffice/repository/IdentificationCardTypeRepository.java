package pt.famility.backoffice.repository;

import pt.famility.backoffice.domain.IdentificationCardType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the IdentificationCardType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdentificationCardTypeRepository extends JpaRepository<IdentificationCardType, Long> {

    Optional<IdentificationCardType> findByName(String name);

}
