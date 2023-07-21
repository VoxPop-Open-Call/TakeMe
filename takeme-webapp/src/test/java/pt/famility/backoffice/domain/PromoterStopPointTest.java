package pt.famility.backoffice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.famility.backoffice.web.rest.TestUtil;

class PromoterStopPointTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromoterStopPoint.class);
        PromoterStopPoint promoterStopPoint1 = new PromoterStopPoint();
        promoterStopPoint1.setId(1L);
        PromoterStopPoint promoterStopPoint2 = new PromoterStopPoint();
        promoterStopPoint2.setId(promoterStopPoint1.getId());
        assertThat(promoterStopPoint1).isEqualTo(promoterStopPoint2);
        promoterStopPoint2.setId(2L);
        assertThat(promoterStopPoint1).isNotEqualTo(promoterStopPoint2);
        promoterStopPoint1.setId(null);
        assertThat(promoterStopPoint1).isNotEqualTo(promoterStopPoint2);
    }
}
