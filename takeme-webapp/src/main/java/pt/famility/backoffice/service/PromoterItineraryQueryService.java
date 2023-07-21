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
import pt.famility.backoffice.domain.PromoterItinerary;
import pt.famility.backoffice.repository.PromoterItineraryRepository;
import pt.famility.backoffice.service.criteria.PromoterItineraryCriteria;
import pt.famility.backoffice.service.dto.PromoterItineraryDTO;
import pt.famility.backoffice.service.mapper.PromoterItineraryMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PromoterItinerary} entities in the database.
 * The main input is a {@link PromoterItineraryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PromoterItineraryDTO} or a {@link Page} of {@link PromoterItineraryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PromoterItineraryQueryService extends QueryService<PromoterItinerary> {

    private final Logger log = LoggerFactory.getLogger(PromoterItineraryQueryService.class);

    private final PromoterItineraryRepository promoterItineraryRepository;

    private final PromoterItineraryMapper promoterItineraryMapper;

    public PromoterItineraryQueryService(
        PromoterItineraryRepository promoterItineraryRepository,
        PromoterItineraryMapper promoterItineraryMapper
    ) {
        this.promoterItineraryRepository = promoterItineraryRepository;
        this.promoterItineraryMapper = promoterItineraryMapper;
    }

    /**
     * Return a {@link List} of {@link PromoterItineraryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PromoterItineraryDTO> findByCriteria(PromoterItineraryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PromoterItinerary> specification = createSpecification(criteria);
        return promoterItineraryMapper.toDto(promoterItineraryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PromoterItineraryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PromoterItineraryDTO> findByCriteria(PromoterItineraryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PromoterItinerary> specification = createSpecification(criteria);
        return promoterItineraryRepository.findAll(specification, page).map(promoterItineraryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PromoterItineraryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PromoterItinerary> specification = createSpecification(criteria);
        return promoterItineraryRepository.count(specification);
    }

    /**
     * Function to convert {@link PromoterItineraryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PromoterItinerary> createSpecification(PromoterItineraryCriteria criteria) {
        Specification<PromoterItinerary> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PromoterItinerary_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PromoterItinerary_.name));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), PromoterItinerary_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), PromoterItinerary_.endDate));
            }
            if (criteria.getServiceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getServiceId(),
                            root -> root.join(PromoterItinerary_.service, JoinType.LEFT).get(PromoterService_.id)
                        )
                    );
            }
            if (criteria.getOrganizationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrganizationId(),
                            root -> root.join(PromoterItinerary_.organization, JoinType.LEFT).get(Organization_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
