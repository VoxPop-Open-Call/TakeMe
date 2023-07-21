package pt.famility.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import pt.famility.backoffice.domain.enumeration.OrganizationType;
import pt.famility.backoffice.domain.enumeration.StatusType;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Organization.
 */
@Entity
@Table(name = "organization")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Organization extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "nif_country", nullable = false)
    private String nifCountry;

    @NotNull
    @Column(name = "nif_number", nullable = false)
    private String nifNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "organization_type", nullable = false)
    private OrganizationType organizationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_type")
    private StatusType statusType;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(unique = true)
    private Location location;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Contact> contacts = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_file_id", nullable=true)
    @Basic(fetch=FetchType.LAZY)
    private DocumentFile photoFile;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "organization_location",
        joinColumns = @JoinColumn(name = "organization_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "location_id", referencedColumnName = "id"))
    private Set<Location> serviceLocations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organization id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Organization name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNifCountry() {
        return nifCountry;
    }

    public Organization nifCountry(String nifCountry) {
        this.nifCountry = nifCountry;
        return this;
    }

    public void setNifCountry(String nifCountry) {
        this.nifCountry = nifCountry;
    }

    public String getNifNumber() {
        return nifNumber;
    }

    public Organization nifNumber(String nifNumber) {
        this.nifNumber = nifNumber;
        return this;
    }

    public void setNifNumber(String nifNumber) {
        this.nifNumber = nifNumber;
    }

    public OrganizationType getOrganizationType() {
        return organizationType;
    }

    public Organization organizationType(OrganizationType organizationType) {
        this.organizationType = organizationType;
        return this;
    }

    public void setOrganizationType(OrganizationType organizationType) {
        this.organizationType = organizationType;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public Organization statusType(StatusType statusType) {
        this.statusType = statusType;
        return this;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public Organization contacts(Set<Contact> contacts) {
        this.contacts = contacts;
        return this;
    }

    public Organization addContact(Contact contact) {
        contact.setOrganization(this);
        this.contacts.add(contact);
        return this;
    }

    public Organization removeContact(Contact contact) {
        this.contacts.remove(contact);
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Organization location(Location location) {
        this.location = location;
        return this;
    }

    public DocumentFile getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(DocumentFile photoFile) {
        this.photoFile = photoFile;
    }

    public Organization photoFile(DocumentFile photoFile) {
        this.photoFile = photoFile;
        return this;
    }

    public Set<Location> getServiceLocations() {
        return serviceLocations;
    }

    public void setServiceLocations(Set<Location> serviceLocations) {
        this.serviceLocations = serviceLocations;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Organization organization = (Organization) o;
        if (organization.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organization.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Organization{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", nifCountry='" + nifCountry + '\'' +
            ", nifNumber='" + nifNumber + '\'' +
            ", organizationType=" + organizationType +
            ", statusType=" + statusType +
            ", location=" + location +
            '}';
    }
}
