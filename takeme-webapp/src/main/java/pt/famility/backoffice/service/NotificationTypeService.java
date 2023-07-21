package pt.famility.backoffice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.famility.backoffice.domain.NotificationType;
import pt.famility.backoffice.domain.enumeration.NotificationTypeType;
import pt.famility.backoffice.repository.NotificationTypeRepository;

import java.security.InvalidParameterException;

@Service
@Transactional
public class NotificationTypeService {

    private NotificationTypeRepository notificationTypeRepository;

    public NotificationTypeService(NotificationTypeRepository notificationTypeRepository) {
        this.notificationTypeRepository = notificationTypeRepository;
    }

    public NotificationType findByType(NotificationTypeType notificationTypeType) {
        return notificationTypeRepository.findByType(notificationTypeType).orElseThrow(() -> new InvalidParameterException("type"));
    }
}
