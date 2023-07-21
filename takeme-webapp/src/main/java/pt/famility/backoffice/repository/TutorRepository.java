package pt.famility.backoffice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.famility.backoffice.domain.Tutor;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Tutor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    @Query("select tutor from Tutor tutor left join fetch tutor.children where tutor.id =:id")
    Optional<Tutor> findOneByUserIdWithEagerRelationships(@Param("id") Long id);

    Optional<Tutor> findByUserId(Long userId);

    Optional<Tutor> findFirstByUserId(Long userId);

    List<Tutor> findAllByUserId(Long userId);

    Optional<Tutor> findByIdAndPhotoFileId(Long id, Long photoFileId);
}
