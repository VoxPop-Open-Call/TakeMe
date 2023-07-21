package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ChildStopPointsServicesDTO {

    ChildDTO child;

    List<ServiceStopPointDTO> serviceStopPoints = new ArrayList<>();
}
