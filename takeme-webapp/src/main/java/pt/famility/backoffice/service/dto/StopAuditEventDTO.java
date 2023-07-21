package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.StopEventType;

import java.time.Instant;

@Data
@NoArgsConstructor
public class StopAuditEventDTO {

    Long id;

    private Instant eventTime;

    private StopEventType eventType;

    private ChildDTO child;
}
