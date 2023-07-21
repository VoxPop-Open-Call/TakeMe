package pt.famility.backoffice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.famility.backoffice.domain.Child;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Child entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {

    @Query(
            value = "select distinct child from Child child left join fetch child.tutors left join fetch child.organizations",
            countQuery = "select count(distinct child) from Child child"
    )
    Page<Child> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct child from Child child left join fetch child.tutors left join fetch child.organizations")
    List<Child> findAllWithEagerRelationships();

    @Query("select child from Child child left join fetch child.tutors left join fetch child.organizations where child.id =:id")
    Optional<Child> findOneWithEagerRelationships(@Param("id") Long id);

    Page<Child> findAllByTutorsId(Long tutorsId, Pageable pageable);

    Optional<Child> findByIdAndPhotoFileId(Long id, Long photoFileId);

    List<Child> findByNifCountryAndNifNumber(String nifCountry, String nifNumber);

    List<Child> findByNifCountryAndNifNumberAndFamility(String nifCountry, String nifNumber, boolean famility);
}
