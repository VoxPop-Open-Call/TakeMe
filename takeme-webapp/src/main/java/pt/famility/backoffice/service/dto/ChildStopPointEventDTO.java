package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.StopEventType;

@Data
@NoArgsConstructor
public class ChildStopPointEventDTO {

    Long childId;

    StopEventType event;
}
