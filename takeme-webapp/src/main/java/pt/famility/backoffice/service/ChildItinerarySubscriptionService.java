package pt.famility.backoffice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.domain.ChildItinerarySubscription;
import pt.famility.backoffice.repository.ChildItinerarySubscriptionRepository;
import pt.famility.backoffice.service.dto.ChildItinerarySubscriptionDTO;
import pt.famility.backoffice.service.mapper.ChildItinerarySubscriptionMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Service Implementation for managing {@link ChildItinerarySubscription}.
 */
@Service
@Transactional
public class ChildItinerarySubscriptionService {

    private final Logger log = LoggerFactory.getLogger(ChildItinerarySubscriptionService.class);

    private final ChildItinerarySubscriptionRepository childItinerarySubscriptionRepository;

    private final ChildItinerarySubscriptionMapper childItinerarySubscriptionMapper;

    public ChildItinerarySubscriptionService(
            ChildItinerarySubscriptionRepository childItinerarySubscriptionRepository,
            ChildItinerarySubscriptionMapper childItinerarySubscriptionMapper
    ) {
        this.childItinerarySubscriptionRepository = childItinerarySubscriptionRepository;
        this.childItinerarySubscriptionMapper = childItinerarySubscriptionMapper;
    }

    /**
     * Save a childItinerarySubscription.
     *
     * @param childItinerarySubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public ChildItinerarySubscriptionDTO save(ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO) {
        log.debug("Request to save ChildItinerarySubscription : {}", childItinerarySubscriptionDTO);
        ChildItinerarySubscription childItinerarySubscription = childItinerarySubscriptionMapper.toEntity(childItinerarySubscriptionDTO);
        childItinerarySubscription = childItinerarySubscriptionRepository.save(childItinerarySubscription);
        return childItinerarySubscriptionMapper.toDto(childItinerarySubscription);
    }

    /**
     * Update a childItinerarySubscription.
     *
     * @param childItinerarySubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public ChildItinerarySubscriptionDTO update(ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO) {
        log.debug("Request to update ChildItinerarySubscription : {}", childItinerarySubscriptionDTO);
        ChildItinerarySubscription childItinerarySubscription = childItinerarySubscriptionMapper.toEntity(childItinerarySubscriptionDTO);
        childItinerarySubscription = childItinerarySubscriptionRepository.save(childItinerarySubscription);
        return childItinerarySubscriptionMapper.toDto(childItinerarySubscription);
    }

    /**
     * Partially update a childItinerarySubscription.
     *
     * @param childItinerarySubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChildItinerarySubscriptionDTO> partialUpdate(ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO) {
        log.debug("Request to partially update ChildItinerarySubscription : {}", childItinerarySubscriptionDTO);
        return childItinerarySubscriptionRepository
                .findById(childItinerarySubscriptionDTO.getId())
                .map(existingChildItinerarySubscription -> {
                    childItinerarySubscriptionMapper.partialUpdate(existingChildItinerarySubscription, childItinerarySubscriptionDTO);
                    return existingChildItinerarySubscription;
                })
                .map(childItinerarySubscriptionRepository::save)
                .map(childItinerarySubscriptionMapper::toDto);
    }

    /**
     * Get all the childItinerarySubscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChildItinerarySubscriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChildItinerarySubscriptions");
        return childItinerarySubscriptionRepository.findAll(pageable).map(childItinerarySubscriptionMapper::toDto);
    }

    /**
     * Get all the childItinerarySubscriptions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ChildItinerarySubscriptionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return childItinerarySubscriptionRepository.findAllWithEagerRelationships(pageable).map(childItinerarySubscriptionMapper::toDto);
    }

    /**
     * Get one childItinerarySubscription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChildItinerarySubscriptionDTO> findOne(Long id) {
        log.debug("Request to get ChildItinerarySubscription : {}", id);
        return childItinerarySubscriptionRepository
                .findOneWithEagerRelationships(id)
                .map(childItinerarySubscriptionMapper::toDto);
    }

    /**
     * Delete the childItinerarySubscription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ChildItinerarySubscription : {}", id);
        childItinerarySubscriptionRepository.deleteById(id);
    }

    /**
     * Delete the childItinerarySubscription by promoterItineraryId.
     *
     * @param id the id of the entity.
     */
    public void deleteByPromoterItineraryId(Long id) {
        log.debug("Request to delete ChildItinerarySubscription by promoterItineraryId: {}", id);
        childItinerarySubscriptionRepository.deleteByPromoterItineraryId(id);
    }

    /**
     * Get all the childItinerarySubscriptions by promoterItineraryId.
     *
     * @param id the id of the PromoterItinerary.
     * @return the list of entities.
     */
    public List<ChildItinerarySubscription> findAllByPromoterItineraryId(Long id) {
        log.debug("Request to get all ChildItinerarySubscriptions by promoterItineraryId: {}", id);
        return childItinerarySubscriptionRepository.findAllByPromoterItineraryId(id);
    }
}
