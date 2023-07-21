package pt.famility.backoffice.service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.config.firebase.FirebaseMessagingErrorType;
import pt.famility.backoffice.domain.Tutor;
import pt.famility.backoffice.domain.User;
import pt.famility.backoffice.domain.UserMessagingToken;
import pt.famility.backoffice.domain.UserNotification;
import pt.famility.backoffice.domain.enumeration.NotificationChannelType;
import pt.famility.backoffice.domain.enumeration.NotificationDataType;
import pt.famility.backoffice.domain.enumeration.NotificationTypeType;
import pt.famility.backoffice.domain.enumeration.OriginType;
import pt.famility.backoffice.repository.NotificationChannelRepository;
import pt.famility.backoffice.web.rest.errors.InvalidUIDException;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationSenderService {

    private final Logger log = LoggerFactory.getLogger(NotificationSenderService.class);

    private final UserMessagingTokenService userMessagingTokenService;

    private final UserNotificationService userNotificationService;

    private final NotificationTypeService notificationTypeService;

    private final NotificationChannelRepository notificationChannelRepository;

    private final TutorService tutorService;

    private final SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("HH:mm");

    public NotificationSenderService(UserMessagingTokenService userMessagingTokenService,
                                     UserNotificationService userNotificationService,
                                     NotificationTypeService notificationTypeService,
                                     NotificationChannelRepository notificationChannelRepository,
                                     TutorService tutorService) {
        this.userMessagingTokenService = userMessagingTokenService;
        this.userNotificationService = userNotificationService;
        this.notificationTypeService = notificationTypeService;
        this.notificationChannelRepository = notificationChannelRepository;
        this.tutorService = tutorService;
    }

    public void sendPushNotificationToTutor(Long tutorId, String title, String body) {
        Tutor t = tutorService.findById(tutorId).orElseThrow(() -> new InvalidUIDException("tutor"));
        List<UserMessagingToken> userMessagingTokenList = userMessagingTokenService.findAllByUser(t.getUser());
        sendNotification(tutorId, title, body, userMessagingTokenList, Optional.empty());
    }

    private void sendNotification(Long targetId, String title, String body, List<UserMessagingToken> userMessagingTokenList, Optional<String> filter) {
        List<User> users = new ArrayList<>();
        userMessagingTokenList.forEach(userMessagingToken -> {
            String token = userMessagingToken.getToken();
            try {
                String mid = sendMessage(token, userMessagingToken.getOrigin(), title, body, filter);
                log.info("Message with id {} sent to {} (target {})", mid, token, targetId);
                users.add(userMessagingToken.getUser());
            } catch (FirebaseMessagingException e) {
                log.warn("Error " + e.getErrorCode() + " sending push notification to " + token + " (target " + targetId + ")", e);
                if (FirebaseMessagingErrorType.REGISTRATION_TOKEN_NOT_REGISTERED.getErrorKey().equals(e.getErrorCode())) {
                    log.warn("Token not registered. Deleting it...");
                    userMessagingTokenService.delete(token);
                }
            }
        });

        users.forEach(user -> userNotificationService.save(
            new UserNotification()
                .user(user)
                .title(title)
                .body(body)
                .sentDate(Instant.now())
                .notificationChannel(notificationChannelRepository.findByType(NotificationChannelType.PUSH_NOTIFICATION).orElseThrow(() -> new InvalidParameterException("channel")))
        ));
    }

    private String sendMessage(String token, OriginType origin, String title, String body, Optional<String> filter) throws FirebaseMessagingException {
        return this.sendMessage(token, origin, title, body, new HashMap<>(), filter);
    }

    public String sendMessage(String token, OriginType origin, String title, String body, Map<NotificationDataType, String> data, Optional<String> filter) throws FirebaseMessagingException {

        data.put(NotificationDataType.MESSAGE_ID, UUID.randomUUID().toString());
        data.put(NotificationDataType.FILTER, filter.orElse(""));
        Map<String, String> dataStrings = data.entrySet()
            .stream()
            .collect(Collectors.toMap(o -> o.getKey().name(), o -> o.getValue()));

        Message message = Message.builder()
            .putAllData(dataStrings)
            .setToken(token)
            .setAndroidConfig(getAndroidConfig(title, body))
            .setApnsConfig(getApnsConfig(title, body))
            .setWebpushConfig(getWebpushConfig(title, body))
            .build();
        return FirebaseMessaging.getInstance().send(message);
    }

    private WebpushConfig getWebpushConfig(String title, String body) {
        return WebpushConfig.builder()
            .setNotification(WebpushNotification.builder()
                .setTitle(title)
                .setBody(body)
                // .setIcon("assets/icon/icon-512x512.png")
                .setRequireInteraction(true)
                .build())
            .build();
    }

    private AndroidConfig getAndroidConfig(String title, String body) {
        return AndroidConfig.builder()
            .setPriority(AndroidConfig.Priority.HIGH)
            .setNotification(AndroidNotification.builder()
                .setTitle(title)
                .setBody(body)
                // .setIcon("assets/icon/icon-512x512.png")
                // .setClickAction("")
                .build())
            .build();
    }

    private ApnsConfig getApnsConfig(String title, String body) {
        return ApnsConfig.builder()
            .putHeader("apns-priority", "10") //high priority
            .setAps(Aps.builder()
                .setContentAvailable(true)
                .setAlert(
                    ApsAlert.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build()
                )
                .build())
            .build();
    }


    // all the following push notifications are sent to the tutors

    // TODO: no futuro convem usar a lingua do user para construir as mensagens...

    public void sendStartItineraryNotificationWithPrivateAsOrigin(Long tutorId, String childName, Instant estimatedTimeOfArrival) { // with School as destiny
        String message = "Iniciou-se um percurso que irá recolher " + childName + ". Hora estimada de recolha " + convertInstantToReadableString(estimatedTimeOfArrival) + ".";
        sendPushNotificationToTutor(tutorId, "Início de percurso", message);
    }

    public void sendCrossNotificationForPrivateLocationCollection(Long tutorId, String childName, Instant estimatedTimeOfArrival) {
        String message = "Veículo está a aproximar-se do ponto de recolha de " + childName + ". Hora estimada de recolha " + convertInstantToReadableString(estimatedTimeOfArrival) + ".";
        sendPushNotificationToTutor(tutorId, "Veículo aproxima-se", message);
    }

    public void sendCheckInNotificationForPrivateLocation(Long tutorId, String childName) {
        String message = "Recolha de " + childName + " foi efetuada no ponto de recolha.";
        sendPushNotificationToTutor(tutorId, "Paragem concluída", message);
    }

    public void sendCheckOutNotificationForSchoolLocation(Long tutorId, String childName) {
        String message = "Entrega de " + childName + " foi efetuada na escola.";
        sendPushNotificationToTutor(tutorId, "Paragem concluída", message);
    }

    // esta notificação à partida não será necessária...
    public void sendStartItineraryNotificationWithSchoolAsOrigin(Long tutorId, Instant estimatedTimeOfArrival) {
        String message = "Iniciou-se um percurso que irá recolher na escola a sua criança. Hora estimada de recolha " + convertInstantToReadableString(estimatedTimeOfArrival) + ".";
        sendPushNotificationToTutor(tutorId, "Início de percurso", message);
    }

    public void sendCheckInNotificationForSchoolLocation(Long tutorId, String childName) {
        String message = "Recolha de " + childName + " foi efetuada na escola.";
        sendPushNotificationToTutor(tutorId, "Paragem concluída", message);
    }

    public void sendCrossNotificationForPrivateLocationDelivery(Long tutorId, String childName, Instant estimatedTimeOfArrival) {
        String message = "Veículo está a aproximar-se do ponto de entrega de " + childName + ". Hora estimada de entrega " + convertInstantToReadableString(estimatedTimeOfArrival) + ".";
        sendPushNotificationToTutor(tutorId, "Veículo aproxima-se", message);
    }

    public void sendCheckOutNotificationForPrivateLocation(Long tutorId, String childName) {
        String message = "Entrega de " + childName + " foi efetuada no ponto de entrega.";
        sendPushNotificationToTutor(tutorId, "Paragem concluída", message);
    }

    public void sendCheckInCancelNotificationForPrivateLocation(Long tutorId, String childName) {
        String message = "Recolha de " + childName + " não se efetuou no ponto de recolha.";
        sendPushNotificationToTutor(tutorId, "Paragem concluída", message);
    }

    public void sendCheckOutCancelNotificationForSchoolLocation(Long tutorId, String childName) {
        String message = "Entrega de " + childName + " não se efetuou na escola.";
        sendPushNotificationToTutor(tutorId, "Paragem concluída", message);
    }

    public void sendCheckInCancelNotificationForSchoolLocation(Long tutorId, String childName) {
        String message = "Recolha de " + childName + " não se efetuou na escola.";
        sendPushNotificationToTutor(tutorId, "Paragem concluída", message);
    }

    public void sendCheckOutCancelNotificationForPrivateLocation(Long tutorId, String childName) {
        String message = "Entrega de " + childName + " não se efetuou no ponto de entrega.";
        sendPushNotificationToTutor(tutorId, "Paragem concluída", message);
    }

    public String convertInstantToReadableString(Instant instant) {
        Date date = Date.from(instant);
        return simpleDateFormatter.format(date);
    }

}
