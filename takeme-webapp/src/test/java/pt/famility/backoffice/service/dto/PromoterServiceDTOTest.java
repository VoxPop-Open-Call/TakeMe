package pt.famility.backoffice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.famility.backoffice.web.rest.TestUtil;

class PromoterServiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromoterServiceDTO.class);
        PromoterServiceDTO promoterServiceDTO1 = new PromoterServiceDTO();
        promoterServiceDTO1.setId(1L);
        PromoterServiceDTO promoterServiceDTO2 = new PromoterServiceDTO();
        assertThat(promoterServiceDTO1).isNotEqualTo(promoterServiceDTO2);
        promoterServiceDTO2.setId(promoterServiceDTO1.getId());
        assertThat(promoterServiceDTO1).isEqualTo(promoterServiceDTO2);
        promoterServiceDTO2.setId(2L);
        assertThat(promoterServiceDTO1).isNotEqualTo(promoterServiceDTO2);
        promoterServiceDTO1.setId(null);
        assertThat(promoterServiceDTO1).isNotEqualTo(promoterServiceDTO2);
    }
}
