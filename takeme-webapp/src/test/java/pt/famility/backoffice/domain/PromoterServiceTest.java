package pt.famility.backoffice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.famility.backoffice.web.rest.TestUtil;

class PromoterServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromoterService.class);
        PromoterService promoterService1 = new PromoterService();
        promoterService1.setId(1L);
        PromoterService promoterService2 = new PromoterService();
        promoterService2.setId(promoterService1.getId());
        assertThat(promoterService1).isEqualTo(promoterService2);
        promoterService2.setId(2L);
        assertThat(promoterService1).isNotEqualTo(promoterService2);
        promoterService1.setId(null);
        assertThat(promoterService1).isNotEqualTo(promoterService2);
    }
}
