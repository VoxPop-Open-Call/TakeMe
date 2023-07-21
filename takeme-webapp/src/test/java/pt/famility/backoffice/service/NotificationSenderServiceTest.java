package pt.famility.backoffice.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.FamilityBackofficeApp;
import pt.famility.backoffice.domain.StopPoint;
import pt.famility.backoffice.domain.Tutor;
import pt.famility.backoffice.domain.enumeration.LocationType;
import pt.famility.backoffice.domain.enumeration.NotificationDataType;
import pt.famility.backoffice.domain.enumeration.OriginType;
import pt.famility.backoffice.domain.enumeration.StopPointType;
import pt.famility.backoffice.repository.ItineraryStopPointRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FamilityBackofficeApp.class)
@Transactional
public class NotificationSenderServiceTest {


    @Autowired
    NotificationSenderService notificationSenderService;

    @Autowired
    ChildService childService;

    @Autowired
    ItineraryStopPointRepository itineraryStopPointRepository;

    @Test
    public void notificationSenderServiceSendMessageTest() {
        Map<NotificationDataType, String> data = new HashMap<>();
        data.put(NotificationDataType.MESSAGE_ID, "random uui");
        String targetToken = "fkeDQVy8Bvo:APA91bGy81KRxe7CCCa5OT2ouyO_t1kPZre6GCAJT6aKOJ_9h57_3RVCJNlzmkzWmjtPHyCsKaIcZlU0UMu6kiU2HAr0e3oG3p1WI6YMGVedye3Cs4z8cYZSNzdngI_LI9POEbBJCMhg";
        try {
            String resultString = notificationSenderService.sendMessage(targetToken, OriginType.NATIVE, "dummy title", "dummy body", data, Optional.empty());
            System.out.println(resultString);
        } catch (FirebaseMessagingException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    @Test
    public void tutorNotificationUseCasestest() {
        notificationSenderService.sendStartItineraryNotificationWithPrivateAsOrigin(1L, "João", Instant.now());
        notificationSenderService.sendCrossNotificationForPrivateLocationCollection(1L, "João", Instant.now());
        notificationSenderService.sendCheckInNotificationForPrivateLocation(1L, "João");
        notificationSenderService.sendCheckOutNotificationForSchoolLocation(1L, "João");

        notificationSenderService.sendStartItineraryNotificationWithSchoolAsOrigin(1L, Instant.now());
        notificationSenderService.sendCheckInNotificationForSchoolLocation(1L, "João");
        notificationSenderService.sendCrossNotificationForPrivateLocationDelivery(1L, "João", Instant.now());
        notificationSenderService.sendCheckOutNotificationForPrivateLocation(1L, "João");

        notificationSenderService.sendCheckInCancelNotificationForPrivateLocation(1L, "João");
        notificationSenderService.sendCheckOutCancelNotificationForSchoolLocation(1L, "João");
        notificationSenderService.sendCheckInCancelNotificationForSchoolLocation(1L, "João");
        notificationSenderService.sendCheckOutCancelNotificationForPrivateLocation(1L, "João");
    }

    @Test
    public void sendStartItineraryNotificationWithPrivateAsOriginTest() {
        itineraryStopPointRepository.findAllByItinerary_Id(2L).forEach(itineraryStopPoint -> {
            StopPoint stopPoint = itineraryStopPoint.getStopPoint();
            if (stopPoint.getStopPointType().equals(StopPointType.COLLECTION) && stopPoint.getLocation().getType().equals(LocationType.PRIVATE)) {
                stopPoint.getChildList().forEach(child -> {
                    if (child.isFamility()) {
                        Set<Tutor> tutors = child.getTutors();
                        tutors.forEach(tutor -> {
                            Instant estimatedTimeOfArrival;
                            if (stopPoint.getEstimatedArriveTime() != null) {
                                estimatedTimeOfArrival = stopPoint.getEstimatedArriveTime();
                            } else {
                                estimatedTimeOfArrival = stopPoint.getScheduledTime();
                            }
                            notificationSenderService.sendStartItineraryNotificationWithPrivateAsOrigin(tutor.getId(), child.getName(), estimatedTimeOfArrival);
                        });
                    }
                });
            }
        });
    }

}

