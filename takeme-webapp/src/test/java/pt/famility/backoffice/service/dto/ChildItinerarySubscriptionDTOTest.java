package pt.famility.backoffice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.famility.backoffice.web.rest.TestUtil;

class ChildItinerarySubscriptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChildItinerarySubscriptionDTO.class);
        ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO1 = new ChildItinerarySubscriptionDTO();
        childItinerarySubscriptionDTO1.setId(1L);
        ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO2 = new ChildItinerarySubscriptionDTO();
        assertThat(childItinerarySubscriptionDTO1).isNotEqualTo(childItinerarySubscriptionDTO2);
        childItinerarySubscriptionDTO2.setId(childItinerarySubscriptionDTO1.getId());
        assertThat(childItinerarySubscriptionDTO1).isEqualTo(childItinerarySubscriptionDTO2);
        childItinerarySubscriptionDTO2.setId(2L);
        assertThat(childItinerarySubscriptionDTO1).isNotEqualTo(childItinerarySubscriptionDTO2);
        childItinerarySubscriptionDTO1.setId(null);
        assertThat(childItinerarySubscriptionDTO1).isNotEqualTo(childItinerarySubscriptionDTO2);
    }
}
