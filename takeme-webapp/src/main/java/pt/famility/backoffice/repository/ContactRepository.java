package pt.famility.backoffice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.famility.backoffice.domain.Contact;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Contact entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findAllByPublicDataIsTrueAndOrganizationId(Long organizationId);

}
