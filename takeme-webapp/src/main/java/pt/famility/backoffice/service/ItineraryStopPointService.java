package pt.famility.backoffice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pt.famility.backoffice.converter.ItineraryStopPointConverter;
import pt.famility.backoffice.domain.ItineraryStopPoint;
import pt.famility.backoffice.repository.ItineraryStopPointRepository;
import pt.famility.backoffice.service.dto.ItineraryStopPointDTO;
import pt.famility.backoffice.web.rest.errors.InvalidIDException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class ItineraryStopPointService {

    private final ItineraryStopPointRepository itineraryStopPointRepository;

    private final ItineraryStopPointConverter itineraryStopPointConverter;

    public ItineraryStopPointService(ItineraryStopPointRepository itineraryStopPointRepository, ItineraryStopPointConverter itineraryStopPointConverter) {
        this.itineraryStopPointRepository = itineraryStopPointRepository;
        this.itineraryStopPointConverter = itineraryStopPointConverter;
    }

    public Page<ItineraryStopPointDTO> listAllItineraryStopPoint(Long childId, Long organizationId, Pageable pageable) {
        Instant today = Instant.now().truncatedTo(ChronoUnit.DAYS);
        return itineraryStopPointRepository.listAllItineraryStopPoint(childId, organizationId, today, pageable).map(itineraryStopPointConverter::convert);
    }

    public ItineraryStopPoint getById(Long itineraryStopPointId) {
        return itineraryStopPointRepository.findById(itineraryStopPointId).orElseThrow(() -> new InvalidIDException("ItineraryStopPointService"));
    }
}
