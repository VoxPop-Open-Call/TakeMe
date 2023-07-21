package pt.famility.backoffice.service;

import org.springframework.stereotype.Service;
import pt.famility.backoffice.domain.Contact;
import pt.famility.backoffice.repository.ContactRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> getOrganizationPublicContactsByLocationId(Long organizationId) {
        return contactRepository.findAllByPublicDataIsTrueAndOrganizationId(organizationId);
    }

}
