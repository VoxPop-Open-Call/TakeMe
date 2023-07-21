package pt.famility.backoffice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.famility.backoffice.web.rest.TestUtil;

class ChildItinerarySubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChildItinerarySubscription.class);
        ChildItinerarySubscription childItinerarySubscription1 = new ChildItinerarySubscription();
        childItinerarySubscription1.setId(1L);
        ChildItinerarySubscription childItinerarySubscription2 = new ChildItinerarySubscription();
        childItinerarySubscription2.setId(childItinerarySubscription1.getId());
        assertThat(childItinerarySubscription1).isEqualTo(childItinerarySubscription2);
        childItinerarySubscription2.setId(2L);
        assertThat(childItinerarySubscription1).isNotEqualTo(childItinerarySubscription2);
        childItinerarySubscription1.setId(null);
        assertThat(childItinerarySubscription1).isNotEqualTo(childItinerarySubscription2);
    }
}
