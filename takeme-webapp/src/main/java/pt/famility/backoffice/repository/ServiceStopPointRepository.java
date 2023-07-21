package pt.famility.backoffice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.famility.backoffice.domain.ServiceStopPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Spring Data JPA repository for the ServiceStopPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceStopPointRepository extends JpaRepository<ServiceStopPoint, Long> {

    @Query(
            value = "select distinct svc_sp from ServiceStopPoint svc_sp left join fetch svc_sp.serviceStopPointDaysOfWeeks",
            countQuery = "select count(distinct svc_sp) from ServiceStopPoint svc_sp"
    )
    Page<ServiceStopPoint> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct svc_sp from ServiceStopPoint svc_sp left join fetch svc_sp.serviceStopPointDaysOfWeeks")
    List<ServiceStopPoint> findAllWithEagerRelationships();

    @Query("select svc_sp from ServiceStopPoint svc_sp left join fetch svc_sp.serviceStopPointDaysOfWeeks where svc_sp.id =:id")
    Optional<ServiceStopPoint> findOneWithEagerRelationships(@Param("id") Long id);

    Stream<ServiceStopPoint> findAllByService_Id(Long serviceId);
}
