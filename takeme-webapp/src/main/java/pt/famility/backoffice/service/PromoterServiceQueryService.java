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
import pt.famility.backoffice.domain.PromoterService;
import pt.famility.backoffice.repository.PromoterServiceRepository;
import pt.famility.backoffice.service.criteria.PromoterServiceCriteria;
import pt.famility.backoffice.service.dto.PromoterServiceDTO;
import pt.famility.backoffice.service.mapper.PromoterServiceMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PromoterService} entities in the database.
 * The main input is a {@link PromoterServiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PromoterServiceDTO} or a {@link Page} of {@link PromoterServiceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PromoterServiceQueryService extends QueryService<PromoterService> {

    private final Logger log = LoggerFactory.getLogger(PromoterServiceQueryService.class);

    private final PromoterServiceRepository promoterServiceRepository;

    private final PromoterServiceMapper promoterServiceMapper;

    public PromoterServiceQueryService(PromoterServiceRepository promoterServiceRepository, PromoterServiceMapper promoterServiceMapper) {
        this.promoterServiceRepository = promoterServiceRepository;
        this.promoterServiceMapper = promoterServiceMapper;
    }

    /**
     * Return a {@link List} of {@link PromoterServiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PromoterServiceDTO> findByCriteria(PromoterServiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PromoterService> specification = createSpecification(criteria);
        return promoterServiceMapper.toDto(promoterServiceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PromoterServiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PromoterServiceDTO> findByCriteria(PromoterServiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PromoterService> specification = createSpecification(criteria);
        return promoterServiceRepository.findAll(specification, page).map(promoterServiceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PromoterServiceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PromoterService> specification = createSpecification(criteria);
        return promoterServiceRepository.count(specification);
    }

    /**
     * Function to convert {@link PromoterServiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PromoterService> createSpecification(PromoterServiceCriteria criteria) {
        Specification<PromoterService> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PromoterService_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PromoterService_.name));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), PromoterService_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), PromoterService_.endDate));
            }
            if (criteria.getNeedsETA() != null) {
                specification = specification.and(buildSpecification(criteria.getNeedsETA(), PromoterService_.needsETA));
            }
            if (criteria.getEnrollmentURL() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEnrollmentURL(), PromoterService_.enrollmentURL));
            }
        }
        return specification;
    }
}
