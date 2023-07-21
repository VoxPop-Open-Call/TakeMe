package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.enumeration.DaysOfWeekType;

@Data
@NoArgsConstructor
public class ServiceStopPointDaysOfWeekDTO {

    private Long id;
    private DaysOfWeekType day;
}
