package pt.famility.backoffice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.famility.backoffice.web.rest.TestUtil;

class PromoterItineraryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PromoterItinerary.class);
        PromoterItinerary promoterItinerary1 = new PromoterItinerary();
        promoterItinerary1.setId(1L);
        PromoterItinerary promoterItinerary2 = new PromoterItinerary();
        promoterItinerary2.setId(promoterItinerary1.getId());
        assertThat(promoterItinerary1).isEqualTo(promoterItinerary2);
        promoterItinerary2.setId(2L);
        assertThat(promoterItinerary1).isNotEqualTo(promoterItinerary2);
        promoterItinerary1.setId(null);
        assertThat(promoterItinerary1).isNotEqualTo(promoterItinerary2);
    }
}
