package pt.famility.backoffice.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.Contact;
import pt.famility.backoffice.domain.Organization;
import pt.famility.backoffice.service.dto.OrganizationDTO;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class OrganizationConverter {

    private final ObjectMapper objectMapper;

    private LocationConverter locationConverter;

    public OrganizationConverter(ObjectMapper objectMapper, LocationConverter locationConverter) {
        this.objectMapper = objectMapper;
        this.locationConverter = locationConverter;
    }

    public OrganizationDTO convert(Organization organization) {
        OrganizationDTO organizationDTO = objectMapper.convertValue(organization, OrganizationDTO.class);
        organizationDTO.setLocation(locationConverter.convert(organization.getLocation()));
        Optional.ofNullable(organization.getPhotoFile()).ifPresent(documentFile -> organizationDTO.setPhotoId(documentFile.getId()));

        Set<Contact> contacts = new HashSet<>();
        organization.getContacts().forEach(contact -> contacts.add(contact));
        organizationDTO.setContacts(contacts);

        return organizationDTO;
    }
}
