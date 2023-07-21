package pt.famility.backoffice.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.domain.*; // for static metamodels
import pt.famility.backoffice.domain.PromoterStopPoint;
import pt.famility.backoffice.repository.PromoterStopPointRepository;
import pt.famility.backoffice.service.criteria.PromoterStopPointCriteria;
import pt.famility.backoffice.service.dto.PromoterStopPointDTO;
import pt.famility.backoffice.service.mapper.PromoterStopPointMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PromoterStopPoint} entities in the database.
 * The main input is a {@link PromoterStopPointCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PromoterStopPointDTO} or a {@link Page} of {@link PromoterStopPointDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PromoterStopPointQueryService extends QueryService<PromoterStopPoint> {

    private final Logger log = LoggerFactory.getLogger(PromoterStopPointQueryService.class);

    private final PromoterStopPointRepository promoterStopPointRepository;

    private final PromoterStopPointMapper promoterStopPointMapper;

    public PromoterStopPointQueryService(
        PromoterStopPointRepository promoterStopPointRepository,
        PromoterStopPointMapper promoterStopPointMapper
    ) {
        this.promoterStopPointRepository = promoterStopPointRepository;
        this.promoterStopPointMapper = promoterStopPointMapper;
    }

    /**
     * Return a {@link List} of {@link PromoterStopPointDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PromoterStopPointDTO> findByCriteria(PromoterStopPointCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PromoterStopPoint> specification = createSpecification(criteria);
        return promoterStopPointMapper.toDto(promoterStopPointRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PromoterStopPointDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PromoterStopPointDTO> findByCriteria(PromoterStopPointCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PromoterStopPoint> specification = createSpecification(criteria);
        return promoterStopPointRepository.findAll(specification, page).map(promoterStopPointMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PromoterStopPointCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PromoterStopPoint> specification = createSpecification(criteria);
        return promoterStopPointRepository.count(specification);
    }

    /**
     * Function to convert {@link PromoterStopPointCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PromoterStopPoint> createSpecification(PromoterStopPointCriteria criteria) {
        Specification<PromoterStopPoint> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PromoterStopPoint_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PromoterStopPoint_.name));
            }
            if (criteria.getScheduledTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScheduledTime(), PromoterStopPoint_.scheduledTime));
            }
            if (criteria.getPromoterItineraryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPromoterItineraryId(),
                            root -> root.join(PromoterStopPoint_.promoterItinerary, JoinType.LEFT).get(PromoterItinerary_.id)
                        )
                    );
            }
            if (criteria.getLocationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLocationId(),
                            root -> root.join(PromoterStopPoint_.location, JoinType.LEFT).get(Location_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
