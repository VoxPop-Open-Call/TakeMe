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
import pt.famility.backoffice.domain.PromoterItinerary;
import pt.famility.backoffice.domain.PromoterStopPoint;
import pt.famility.backoffice.repository.PromoterItineraryRepository;
import pt.famility.backoffice.repository.PromoterStopPointRepository;
import pt.famility.backoffice.service.dto.PromoterItineraryDTO;
import pt.famility.backoffice.service.mapper.PromoterItineraryMapper;
import pt.famility.backoffice.service.mapper.PromoterStopPointMapper;

/**
 * Service Implementation for managing {@link PromoterItinerary}.
 */
@Service
@Transactional
public class PromoterItineraryService {

    private final Logger log = LoggerFactory.getLogger(PromoterItineraryService.class);

    private final PromoterItineraryRepository promoterItineraryRepository;

    private final PromoterStopPointRepository promoterStopPointRepository;

    private final PromoterItineraryMapper promoterItineraryMapper;

    private final PromoterStopPointMapper promoterStopPointMapper;

    public PromoterItineraryService(
        PromoterItineraryRepository promoterItineraryRepository,
        PromoterStopPointRepository promoterStopPointRepository,
        PromoterItineraryMapper promoterItineraryMapper,
        PromoterStopPointMapper promoterStopPointMapper
    ) {
        this.promoterItineraryRepository = promoterItineraryRepository;
        this.promoterStopPointRepository = promoterStopPointRepository;
        this.promoterItineraryMapper = promoterItineraryMapper;
        this.promoterStopPointMapper = promoterStopPointMapper;
    }

    /**
     * Save a promoterItinerary.
     *
     * @param promoterItineraryDTO the entity to save.
     * @return the persisted entity.
     */
    public PromoterItineraryDTO save(PromoterItineraryDTO promoterItineraryDTO) {
        PromoterItinerary itinerarySaved = promoterItineraryRepository.save(promoterItineraryMapper.toEntity(promoterItineraryDTO));

        if (!promoterItineraryDTO.getPromoterItineraryStopPoints().isEmpty()){
            saveStopPoints(promoterStopPointMapper.toEntity(promoterItineraryDTO.getPromoterItineraryStopPoints()), promoterItineraryDTO);
        }

        return promoterItineraryMapper.toDto(itinerarySaved);
    }

    /**
     * Update a promoterItinerary.
     *
     * @param promoterItineraryDTO the entity to save.
     * @return the persisted entity.
     */
    public PromoterItineraryDTO update(PromoterItineraryDTO promoterItineraryDTO) {
        log.debug("Request to update PromoterItinerary : {}", promoterItineraryDTO);

        if (!promoterItineraryDTO.getPromoterItineraryStopPoints().isEmpty()){
            saveStopPoints(promoterStopPointMapper.toEntity(promoterItineraryDTO.getPromoterItineraryStopPoints()), promoterItineraryDTO);
        }

        return promoterItineraryMapper.toDto(promoterItineraryRepository.save(promoterItineraryMapper.toEntity(promoterItineraryDTO)));
    }

    /**
     * Partially update a promoterItinerary.
     *
     * @param promoterItineraryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PromoterItineraryDTO> partialUpdate(PromoterItineraryDTO promoterItineraryDTO) {
        log.debug("Request to partially update PromoterItinerary : {}", promoterItineraryDTO);

        return promoterItineraryRepository
            .findById(promoterItineraryDTO.getId())
            .map(existingPromoterItinerary -> {
                promoterItineraryMapper.partialUpdate(existingPromoterItinerary, promoterItineraryDTO);

                return existingPromoterItinerary;
            })
            .map(promoterItineraryRepository::save)
            .map(promoterItineraryMapper::toDto);
    }

    /**
     * Get all the promoterItineraries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PromoterItineraryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PromoterItineraries");
        return promoterItineraryRepository.findAll(pageable).map(promoterItineraryMapper::toDto);
    }

    /**
     * Get all the promoterItineraries with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PromoterItineraryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return promoterItineraryRepository.findAllWithEagerRelationships(pageable).map(promoterItineraryMapper::toDto);
    }

    /**
     * Get one promoterItinerary by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PromoterItineraryDTO> findOne(Long id) {
        log.debug("Request to get PromoterItinerary : {}", id);
        return promoterItineraryRepository.findOneWithEagerRelationships(id).map(promoterItineraryMapper::toDto);
    }

    /**
     * Delete the promoterItinerary by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PromoterItinerary : {}", id);
        promoterItineraryRepository.deleteById(id);
    }

    /**
     * Get all the PromoterItineraries given the PromoterService id.
     *
     * @param serviceId the PromoterService id.
     * @return the list of PromoterItineraries.
     */
    public List<PromoterItineraryDTO> findPromoterItinerariesByPromoterServiceId(Long serviceId) {
        log.debug("Request to get all PromoterItineraries for id : {}", serviceId);
        return promoterItineraryRepository
            .findByServiceId(serviceId)
            .map(promoterItineraryMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get all the active PromoterItineraries given the PromoterService id.
     *
     * @param serviceId the PromoterService id.
     * @return the list of active PromoterItineraries.
     */
    public List<PromoterItineraryDTO> findActivePromoterItinerariesByPromoterServiceId(Long serviceId) {
        log.debug("Request to get all active PromoterItineraries for id : {}", serviceId);
        return promoterItineraryRepository
            .findByServiceIdAndEndDateNull(serviceId)
            .map(promoterItineraryMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get all the PromoterItineraries by Organization id.
     *
     * @param id       the Organization id.
     * @param pageable the pagination information.
     * @return the list of PromoterItineraries.
     */
    public Page<PromoterItineraryDTO> findPromoterItinerariesByOrganizationId(Long id, Pageable pageable) {
        log.debug("Request to get all PromoterItineraries for id : {}", id);
        return promoterItineraryRepository
            .findByOrganizationId(id, pageable)
            .map(promoterItineraryMapper::toDto);
    }

    private void saveStopPoints(List<PromoterStopPoint> promoterStopPoints, PromoterItineraryDTO promoterItineraryDTO){
        promoterStopPoints.forEach(
            promoterStopPoint -> promoterStopPoint.setPromoterItinerary(
                promoterItineraryMapper.toEntity(promoterItineraryDTO)));

        promoterStopPointRepository.saveAll(promoterStopPoints);
    }
}
