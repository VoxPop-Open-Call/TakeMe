package pt.famility.backoffice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChildItinerarySubscriptionMapperTest {

    private ChildItinerarySubscriptionMapper childItinerarySubscriptionMapper;

    @BeforeEach
    public void setUp() {
        childItinerarySubscriptionMapper = new ChildItinerarySubscriptionMapperImpl();
    }
}
