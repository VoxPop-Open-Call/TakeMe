package pt.famility.backoffice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.famility.backoffice.web.rest.TestUtil;

class PromoterItineraryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromoterItineraryDTO.class);
        PromoterItineraryDTO promoterItineraryDTO1 = new PromoterItineraryDTO();
        promoterItineraryDTO1.setId(1L);
        PromoterItineraryDTO promoterItineraryDTO2 = new PromoterItineraryDTO();
        assertThat(promoterItineraryDTO1).isNotEqualTo(promoterItineraryDTO2);
        promoterItineraryDTO2.setId(promoterItineraryDTO1.getId());
        assertThat(promoterItineraryDTO1).isEqualTo(promoterItineraryDTO2);
        promoterItineraryDTO2.setId(2L);
        assertThat(promoterItineraryDTO1).isNotEqualTo(promoterItineraryDTO2);
        promoterItineraryDTO1.setId(null);
        assertThat(promoterItineraryDTO1).isNotEqualTo(promoterItineraryDTO2);
    }
}
