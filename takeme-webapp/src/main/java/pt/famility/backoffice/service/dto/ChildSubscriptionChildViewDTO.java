package pt.famility.backoffice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pt.famility.backoffice.domain.Contact;
import pt.famility.backoffice.domain.enumeration.StatusType;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ChildSubscriptionChildViewDTO {

    private Long id;

    private OrganizationDTO organization;

    private Long childId;

    private List<Contact> organizationContacts;

    private String comments;

    private StatusType statusType;

    private List<ItineraryStopPointDTO> itineraryStopPoints = new ArrayList<>();
}
