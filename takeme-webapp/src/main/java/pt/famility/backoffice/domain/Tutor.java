package pt.famility.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Tutor.
 */
@Entity
@Table(name = "tutor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tutor extends AbstractAuditingEntity implements Serializable {

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
    @Column(name = "identification_card_number", nullable = false)
    private String identificationCardNumber;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "famility")
    private Boolean famility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_file_id", nullable=true)
    @Basic(fetch=FetchType.LAZY)
    private DocumentFile photoFile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_type")
    private StatusType statusType;

    @ManyToOne
    private IdentificationCardType identificationCardType;

    @ManyToMany(mappedBy = "tutors")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Child> children = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_tutor__location",
        joinColumns = @JoinColumn(name = "tutor_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tutors", "organizations" }, allowSetters = true)
    private Set<Location> locations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Tutor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNifCountry() {
        return nifCountry;
    }

    public Tutor nifCountry(String nifCountry) {
        this.nifCountry = nifCountry;
        return this;
    }

    public void setNifCountry(String nifCountry) {
        this.nifCountry = nifCountry;
    }

    public String getNifNumber() {
        return nifNumber;
    }

    public Tutor nifNumber(String nifNumber) {
        this.nifNumber = nifNumber;
        return this;
    }

    public void setNifNumber(String nifNumber) {
        this.nifNumber = nifNumber;
    }

    public String getIdentificationCardNumber() {
        return identificationCardNumber;
    }

    public Tutor identificationCardNumber(String identificationCardNumber) {
        this.identificationCardNumber = identificationCardNumber;
        return this;
    }

    public void setIdentificationCardNumber(String identificationCardNumber) {
        this.identificationCardNumber = identificationCardNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Tutor phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean isFamility() {
        return famility;
    }

    public Tutor famility(Boolean famility) {
        this.famility = famility;
        return this;
    }

    public void setFamility(Boolean famility) {
        this.famility = famility;
    }

    public DocumentFile getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(DocumentFile photoFile) {
        this.photoFile = photoFile;
    }

    public Tutor photoFile(DocumentFile photoFile) {
        this.photoFile = photoFile;
        return this;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public Tutor statusType(StatusType statusType) {
        this.statusType = statusType;
        return this;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public IdentificationCardType getIdentificationCardType() {
        return identificationCardType;
    }

    public Tutor identificationCardType(IdentificationCardType identificationCardType) {
        this.identificationCardType = identificationCardType;
        return this;
    }

    public void setIdentificationCardType(IdentificationCardType identificationCardType) {
        this.identificationCardType = identificationCardType;
    }

    public Set<Child> getChildren() {
        return children;
    }

    public Tutor children(Set<Child> children) {
        this.children = children;
        return this;
    }

    public Tutor addChild(Child child) {
        this.children.add(child);
        child.getTutors().add(this);
        return this;
    }

    public Tutor removeChild(Child child) {
        this.children.remove(child);
        child.getTutors().remove(this);
        return this;
    }

    public void setChildren(Set<Child> children) {
        this.children = children;
    }

    public Set<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Tutor locations(Set<Location> locations) {
        this.setLocations(locations);
        return this;
    }

    public Tutor addLocation(Location location) {
        this.locations.add(location);
        location.getTutors().add(this);
        return this;
    }

    public Tutor removeLocation(Location location) {
        this.locations.remove(location);
        location.getTutors().remove(this);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tutor user(User user) {
        this.user = user;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tutor tutor = (Tutor) o;
        if (tutor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tutor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tutor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nifCountry='" + getNifCountry() + "'" +
            ", nifNumber='" + getNifNumber() + "'" +
            ", identificationCardNumber='" + getIdentificationCardNumber() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", famility='" + isFamility() + "'" +
            ", statusType='" + getStatusType() + "'" +
            "}";
    }
}
