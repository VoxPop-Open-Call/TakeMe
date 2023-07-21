package pt.famility.backoffice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PromoterItineraryMapperTest {

    private PromoterItineraryMapper promoterItineraryMapper;

    @BeforeEach
    public void setUp() {
        promoterItineraryMapper = new PromoterItineraryMapperImpl();
    }
}
