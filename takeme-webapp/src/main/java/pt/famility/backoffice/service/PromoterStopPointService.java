package pt.famility.backoffice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.domain.PromoterStopPoint;
import pt.famility.backoffice.repository.PromoterStopPointRepository;
import pt.famility.backoffice.service.dto.PromoterStopPointDTO;
import pt.famility.backoffice.service.mapper.PromoterStopPointMapper;

/**
 * Service Implementation for managing {@link PromoterStopPoint}.
 */
@Service
@Transactional
public class PromoterStopPointService {

    private final Logger log = LoggerFactory.getLogger(PromoterStopPointService.class);

    private final PromoterStopPointRepository promoterStopPointRepository;

    private final PromoterStopPointMapper promoterStopPointMapper;

    public PromoterStopPointService(
        PromoterStopPointRepository promoterStopPointRepository,
        PromoterStopPointMapper promoterStopPointMapper
    ) {
        this.promoterStopPointRepository = promoterStopPointRepository;
        this.promoterStopPointMapper = promoterStopPointMapper;
    }

    /**
     * Save a promoterStopPoint.
     *
     * @param promoterStopPointDTO the entity to save.
     * @return the persisted entity.
     */
    public PromoterStopPointDTO save(PromoterStopPointDTO promoterStopPointDTO) {
        log.debug("Request to save PromoterStopPoint : {}", promoterStopPointDTO);
        PromoterStopPoint promoterStopPoint = promoterStopPointMapper.toEntity(promoterStopPointDTO);
        promoterStopPoint = promoterStopPointRepository.save(promoterStopPoint);
        return promoterStopPointMapper.toDto(promoterStopPoint);
    }

    /**
     * Update a promoterStopPoint.
     *
     * @param promoterStopPointDTO the entity to save.
     * @return the persisted entity.
     */
    public PromoterStopPointDTO update(PromoterStopPointDTO promoterStopPointDTO) {
        log.debug("Request to update PromoterStopPoint : {}", promoterStopPointDTO);
        PromoterStopPoint promoterStopPoint = promoterStopPointMapper.toEntity(promoterStopPointDTO);
        promoterStopPoint = promoterStopPointRepository.save(promoterStopPoint);
        return promoterStopPointMapper.toDto(promoterStopPoint);
    }

    /**
     * Partially update a promoterStopPoint.
     *
     * @param promoterStopPointDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PromoterStopPointDTO> partialUpdate(PromoterStopPointDTO promoterStopPointDTO) {
        log.debug("Request to partially update PromoterStopPoint : {}", promoterStopPointDTO);

        return promoterStopPointRepository
            .findById(promoterStopPointDTO.getId())
            .map(existingPromoterStopPoint -> {
                promoterStopPointMapper.partialUpdate(existingPromoterStopPoint, promoterStopPointDTO);

                return existingPromoterStopPoint;
            })
            .map(promoterStopPointRepository::save)
            .map(promoterStopPointMapper::toDto);
    }

    /**
     * Get all the promoterStopPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PromoterStopPointDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PromoterStopPoints");
        return promoterStopPointRepository.findAll(pageable).map(promoterStopPointMapper::toDto);
    }

    /**
     * Get all the promoterStopPoints with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PromoterStopPointDTO> findAllWithEagerRelationships(Pageable pageable) {
        return promoterStopPointRepository.findAllWithEagerRelationships(pageable).map(promoterStopPointMapper::toDto);
    }

    /**
     * Get one promoterStopPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PromoterStopPointDTO> findOne(Long id) {
        log.debug("Request to get PromoterStopPoint : {}", id);
        return promoterStopPointRepository.findOneWithEagerRelationships(id).map(promoterStopPointMapper::toDto);
    }

    /**
     * Delete the promoterStopPoint by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PromoterStopPoint : {}", id);
        promoterStopPointRepository.deleteById(id);
    }

    /**
     */
    public List<PromoterStopPointDTO> findPromoterStopPointByPromoterItineraryId(Long id) {
        log.debug("Request to  PromoterStopPoint : {}", id);
        return promoterStopPointRepository
            .findByPromoterItineraryId(id)
            .stream()
            .map(promoterStopPointMapper::toDto)
            .collect(Collectors.toList());
    }
}
