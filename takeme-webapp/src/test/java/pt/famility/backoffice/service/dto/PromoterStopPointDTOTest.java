package pt.famility.backoffice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.famility.backoffice.web.rest.TestUtil;

class PromoterStopPointDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromoterStopPointDTO.class);
        PromoterStopPointDTO promoterStopPointDTO1 = new PromoterStopPointDTO();
        promoterStopPointDTO1.setId(1L);
        PromoterStopPointDTO promoterStopPointDTO2 = new PromoterStopPointDTO();
        assertThat(promoterStopPointDTO1).isNotEqualTo(promoterStopPointDTO2);
        promoterStopPointDTO2.setId(promoterStopPointDTO1.getId());
        assertThat(promoterStopPointDTO1).isEqualTo(promoterStopPointDTO2);
        promoterStopPointDTO2.setId(2L);
        assertThat(promoterStopPointDTO1).isNotEqualTo(promoterStopPointDTO2);
        promoterStopPointDTO1.setId(null);
        assertThat(promoterStopPointDTO1).isNotEqualTo(promoterStopPointDTO2);
    }
}
