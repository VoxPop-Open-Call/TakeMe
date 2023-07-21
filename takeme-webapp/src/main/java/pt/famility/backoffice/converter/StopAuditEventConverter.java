package pt.famility.backoffice.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.StopAuditEvent;
import pt.famility.backoffice.service.dto.StopAuditEventDTO;

@Component
public class StopAuditEventConverter {

    private ObjectMapper objectMapper;

    public StopAuditEventConverter(ObjectMapper objectMapper) { this.objectMapper = objectMapper; }

    public StopAuditEventDTO converter (StopAuditEvent stopAuditEvent) {
        return objectMapper.convertValue(stopAuditEvent, StopAuditEventDTO.class);
    }
}
