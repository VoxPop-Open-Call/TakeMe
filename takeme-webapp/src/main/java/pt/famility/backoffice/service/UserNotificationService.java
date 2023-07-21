package pt.famility.backoffice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.domain.UserNotification;
import pt.famility.backoffice.repository.UserNotificationRepository;

@Service
@Transactional
public class UserNotificationService {

    private UserNotificationRepository userNotificationRepository;


    public UserNotificationService(UserNotificationRepository userNotificationRepository) {
        this.userNotificationRepository = userNotificationRepository;
    }

    public UserNotification save(UserNotification userNotification) {
        return userNotificationRepository.save(userNotification);
    }
}
