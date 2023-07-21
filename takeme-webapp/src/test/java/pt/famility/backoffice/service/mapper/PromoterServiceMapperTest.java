package pt.famility.backoffice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PromoterServiceMapperTest {

    private PromoterServiceMapper promoterServiceMapper;

    @BeforeEach
    public void setUp() {
        promoterServiceMapper = new PromoterServiceMapperImpl();
    }
}
