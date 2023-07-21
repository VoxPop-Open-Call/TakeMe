package pt.famility.backoffice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PromoterStopPointMapperTest {

    private PromoterStopPointMapper promoterStopPointMapper;

    @BeforeEach
    public void setUp() {
        promoterStopPointMapper = new PromoterStopPointMapperImpl();
    }
}
