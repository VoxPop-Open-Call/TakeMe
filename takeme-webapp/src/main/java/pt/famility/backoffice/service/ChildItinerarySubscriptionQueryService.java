package pt.famility.backoffice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.domain.ChildItinerarySubscription;
import pt.famility.backoffice.domain.ChildItinerarySubscription_;
import pt.famility.backoffice.domain.Child_;
import pt.famility.backoffice.domain.Organization_;
import pt.famility.backoffice.domain.PromoterItinerary_;
import pt.famility.backoffice.domain.PromoterService_;
import pt.famility.backoffice.domain.User_;
import pt.famility.backoffice.repository.ChildItinerarySubscriptionRepository;
import pt.famility.backoffice.service.criteria.ChildItinerarySubscriptionCriteria;
import pt.famility.backoffice.service.dto.ChildItinerarySubscriptionDTO;
import pt.famility.backoffice.service.mapper.ChildItinerarySubscriptionMapper;
import tech.jhipster.service.QueryService;

import javax.persistence.criteria.JoinType;
import java.util.List;

/**
 * Service for executing complex queries for {@link ChildItinerarySubscription} entities in the database.
 * The main input is a {@link ChildItinerarySubscriptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChildItinerarySubscriptionDTO} or a {@link Page} of {@link ChildItinerarySubscriptionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChildItinerarySubscriptionQueryService extends QueryService<ChildItinerarySubscription> {

    private final Logger log = LoggerFactory.getLogger(ChildItinerarySubscriptionQueryService.class);

    private final ChildItinerarySubscriptionRepository childItinerarySubscriptionRepository;

    private final ChildItinerarySubscriptionMapper childItinerarySubscriptionMapper;

    public ChildItinerarySubscriptionQueryService(
            ChildItinerarySubscriptionRepository childItinerarySubscriptionRepository,
            ChildItinerarySubscriptionMapper childItinerarySubscriptionMapper
    ) {
        this.childItinerarySubscriptionRepository = childItinerarySubscriptionRepository;
        this.childItinerarySubscriptionMapper = childItinerarySubscriptionMapper;
    }

    /**
     * Return a {@link List} of {@link ChildItinerarySubscriptionDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChildItinerarySubscriptionDTO> findByCriteria(ChildItinerarySubscriptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChildItinerarySubscription> specification = createSpecification(criteria);
        return childItinerarySubscriptionMapper.toDto(childItinerarySubscriptionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChildItinerarySubscriptionDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChildItinerarySubscriptionDTO> findByCriteria(ChildItinerarySubscriptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChildItinerarySubscription> specification = createSpecification(criteria);
        return childItinerarySubscriptionRepository.findAll(specification, page).map(childItinerarySubscriptionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChildItinerarySubscriptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChildItinerarySubscription> specification = createSpecification(criteria);
        return childItinerarySubscriptionRepository.count(specification);
    }

    /**
     * Function to convert {@link ChildItinerarySubscriptionCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChildItinerarySubscription> createSpecification(ChildItinerarySubscriptionCriteria criteria) {
        Specification<ChildItinerarySubscription> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification =
                        specification.and(
                                distinct(criteria.getDistinct())
                        );
            }
            if (criteria.getId() != null) {
                specification =
                        specification.and(
                                buildRangeSpecification(criteria.getId(), ChildItinerarySubscription_.id)
                        );
            }
            if (criteria.getStatusType() != null) {
                specification =
                        specification.and(
                                buildSpecification(criteria.getStatusType(), ChildItinerarySubscription_.statusType)
                        );
            }
            if (criteria.getSubscriptionDate() != null) {
                specification =
                        specification.and(
                                buildRangeSpecification(criteria.getSubscriptionDate(), ChildItinerarySubscription_.subscriptionDate)
                        );
            }
            if (criteria.getActivationDate() != null) {
                specification =
                        specification.and(
                                buildRangeSpecification(criteria.getActivationDate(), ChildItinerarySubscription_.activationDate)
                        );
            }
            if (criteria.getDeactivationDate() != null) {
                specification =
                        specification.and(
                                buildRangeSpecification(criteria.getDeactivationDate(), ChildItinerarySubscription_.deactivationDate)
                        );
            }
            if (criteria.getComments() != null) {
                specification =
                        specification.and(
                                buildStringSpecification(criteria.getComments(), ChildItinerarySubscription_.comments)
                        );
            }
            if (criteria.getAdditionalInformation() != null) {
                specification =
                        specification.and(
                                buildStringSpecification(criteria.getAdditionalInformation(), ChildItinerarySubscription_.additionalInformation)
                        );
            }
            if (criteria.getCardNumber() != null) {
                specification =
                        specification.and(
                                buildStringSpecification(criteria.getCardNumber(), ChildItinerarySubscription_.cardNumber)
                        );
            }
            if (criteria.getChildId() != null) {
                specification =
                        specification.and(
                                buildSpecification(
                                        criteria.getChildId(),
                                        root -> root.join(ChildItinerarySubscription_.child, JoinType.LEFT).get(Child_.id)
                                )
                        );
            }
            if (criteria.getChildName() != null) {
                specification =
                        specification.and(
                                buildSpecification(
                                        criteria.getChildName(),
                                        root -> root.join(ChildItinerarySubscription_.child, JoinType.LEFT).get(Child_.name)
                                )
                        );
            }
            if (criteria.getPromoterItineraryId() != null) {
                specification =
                        specification.and(
                                buildSpecification(
                                        criteria.getPromoterItineraryId(),
                                        root -> root.join(ChildItinerarySubscription_.promoterItinerary, JoinType.LEFT).get(PromoterItinerary_.id)
                                )
                        );
            }
            if (criteria.getPromoterItineraryName() != null) {
                specification =
                        specification.and(
                                buildSpecification(
                                        criteria.getPromoterItineraryName(),
                                        root -> root.join(ChildItinerarySubscription_.promoterItinerary, JoinType.LEFT).get(PromoterItinerary_.name)
                                )
                        );
            }
            if (criteria.getOrganizationId() != null) {
                specification =
                        specification.and(
                                buildSpecification(
                                        criteria.getOrganizationId(),
                                        root -> root.join(ChildItinerarySubscription_.promoterItinerary, JoinType.LEFT).join(PromoterItinerary_.organization, JoinType.LEFT).get(Organization_.id)
                                )
                        );
            }
            if (criteria.getPromoterServiceId() != null) {
                specification =
                        specification.and(
                                buildSpecification(
                                        criteria.getPromoterServiceId(),
                                        root -> root.join(ChildItinerarySubscription_.promoterItinerary, JoinType.LEFT).join(PromoterItinerary_.service, JoinType.LEFT).get(PromoterService_.id)
                                )
                        );
            }
            if (criteria.getUserId() != null) {
                specification =
                        specification.and(
                                buildSpecification(
                                        criteria.getUserId(),
                                        root -> root.join(ChildItinerarySubscription_.user, JoinType.LEFT).get(User_.id)
                                )
                        );
            }
        }
        return specification;
    }
}
