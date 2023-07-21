package pt.famility.backoffice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.domain.PromoterService;
import pt.famility.backoffice.repository.PromoterServiceRepository;
import pt.famility.backoffice.service.dto.PromoterServiceDTO;
import pt.famility.backoffice.service.mapper.PromoterServiceMapper;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PromoterService}.
 */
@Service
@Transactional
public class PromoterServiceService {

    private final Logger log = LoggerFactory.getLogger(PromoterServiceService.class);

    private final PromoterServiceRepository promoterServiceRepository;

    private final PromoterServiceMapper promoterServiceMapper;

    public PromoterServiceService(PromoterServiceRepository promoterServiceRepository, PromoterServiceMapper promoterServiceMapper) {
        this.promoterServiceRepository = promoterServiceRepository;
        this.promoterServiceMapper = promoterServiceMapper;
    }

    /**
     * Save a promoterService.
     *
     * @param promoterServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public PromoterServiceDTO save(PromoterServiceDTO promoterServiceDTO) {
        log.debug("Request to save PromoterService : {}", promoterServiceDTO);
        PromoterService promoterService = promoterServiceMapper.toEntity(promoterServiceDTO);
        promoterService = promoterServiceRepository.save(promoterService);
        return promoterServiceMapper.toDto(promoterService);
    }

    /**
     * Update a promoterService.
     *
     * @param promoterServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public PromoterServiceDTO update(PromoterServiceDTO promoterServiceDTO) {
        log.debug("Request to update PromoterService : {}", promoterServiceDTO);
        PromoterService promoterService = promoterServiceMapper.toEntity(promoterServiceDTO);
        promoterService = promoterServiceRepository.save(promoterService);
        return promoterServiceMapper.toDto(promoterService);
    }

    /**
     * Partially update a promoterService.
     *
     * @param promoterServiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PromoterServiceDTO> partialUpdate(PromoterServiceDTO promoterServiceDTO) {
        log.debug("Request to partially update PromoterService : {}", promoterServiceDTO);

        return promoterServiceRepository
                .findById(promoterServiceDTO.getId())
                .map(existingPromoterService -> {
                    promoterServiceMapper.partialUpdate(existingPromoterService, promoterServiceDTO);

                    return existingPromoterService;
                })
                .map(promoterServiceRepository::save)
                .map(promoterServiceMapper::toDto);
    }

    /**
     * Get all the promoterServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PromoterServiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PromoterServices");
        return promoterServiceRepository.findAll(pageable).map(promoterServiceMapper::toDto);
    }

    /**
     * Get one promoterService by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PromoterServiceDTO> findOne(Long id) {
        log.debug("Request to get PromoterService : {}", id);
        return promoterServiceRepository
                .findById(id)
                .map(promoterServiceMapper::toDto);
    }

    /**
     * Delete the promoterService by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PromoterService : {}", id);
        promoterServiceRepository.deleteById(id);
    }
}
