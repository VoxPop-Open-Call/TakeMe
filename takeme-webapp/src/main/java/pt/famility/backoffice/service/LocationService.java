package pt.famility.backoffice.service;

import org.springframework.stereotype.Service;
import pt.famility.backoffice.converter.LocationConverter;
import pt.famility.backoffice.domain.Contact;
import pt.famility.backoffice.domain.Location;
import pt.famility.backoffice.domain.Organization;
import pt.famility.backoffice.repository.LocationRepository;
import pt.famility.backoffice.service.dto.LocationDTO;
import pt.famility.backoffice.web.rest.errors.InvalidIDException;
import pt.famility.backoffice.web.rest.errors.NotFoundAlertException;

import java.util.List;

@Service
public class LocationService {

    private LocationRepository locationRepository;

    private LocationConverter locationConverter;

    private OrganizationService organizationService;

    private ContactService contactService;

    public LocationService(LocationRepository locationRepository,
                           LocationConverter locationConverter,
                           OrganizationService organizationService,
                           ContactService contactService) {
        this.locationRepository = locationRepository;
        this.locationConverter = locationConverter;
        this.organizationService = organizationService;
        this.contactService = contactService;
    }

    public LocationDTO convert(Location location) {
        return locationConverter.convert(location);
    }

    /**
     * Search for a cecrtain location given the id
     *
     * @param id location id to search
     * @throws NotFoundAlertException if does not existe
     * @return location in the system
     */
    public Location findById(Long id) {
        Location location = locationRepository.findById(id)
            .orElseThrow(() -> new NotFoundAlertException("Location id does not exist", "location", "id"));

        return location;
    }

    public List<Contact> getOrganizationPublicContactsByLocationId(Long locationId) {
        Organization organization = organizationService.getOrganizationByLocationId(locationId);
        return contactService.getOrganizationPublicContactsByLocationId(organization.getId());
    }

}
