package pt.famility.backoffice.service;

import org.springframework.stereotype.Service;
import pt.famility.backoffice.domain.StopPoint;
import pt.famility.backoffice.repository.StopPointRepository;

@Service
public class StopPointService {

    private final StopPointRepository stopPointRepository;

    public StopPointService(StopPointRepository stopPointRepository) {
        this.stopPointRepository = stopPointRepository;
    }

    public StopPoint getById(Long id) {
        return this.stopPointRepository.getOne(id);
    }
}
