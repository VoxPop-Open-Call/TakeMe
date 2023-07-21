package pt.famility.backoffice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import pt.famility.backoffice.domain.enumeration.StatusType;

/**
 * A Child.
 */
@Entity
@Table(name = "child")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Child implements Serializable {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_file_id", nullable=true)
    @Basic(fetch=FetchType.LAZY)
    private DocumentFile photoFile;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "famility")
    private Boolean famility;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_type")
    private StatusType statusType;

    @ManyToMany(cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "child_tutor",
               joinColumns = @JoinColumn(name = "children_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "tutors_id", referencedColumnName = "id"))
    private Set<Tutor> tutors = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "child_organization",
               joinColumns = @JoinColumn(name = "children_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "organizations_id", referencedColumnName = "id"))
    private Set<Organization> organizations = new HashSet<>();

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

    public Child name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNifCountry() {
        return nifCountry;
    }

    public Child nifCountry(String nifCountry) {
        this.nifCountry = nifCountry;
        return this;
    }

    public void setNifCountry(String nifCountry) {
        this.nifCountry = nifCountry;
    }

    public String getNifNumber() {
        return nifNumber;
    }

    public Child nifNumber(String nifNumber) {
        this.nifNumber = nifNumber;
        return this;
    }

    public void setNifNumber(String nifNumber) {
        this.nifNumber = nifNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Child dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean isFamility() {
        return famility;
    }

    public Child famility(Boolean famility) {
        this.famility = famility;
        return this;
    }

    public void setFamility(Boolean famility) {
        this.famility = famility;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public Child statusType(StatusType statusType) {
        this.statusType = statusType;
        return this;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public Set<Tutor> getTutors() {
        return tutors;
    }

    public Child tutors(Set<Tutor> tutors) {
        this.tutors = tutors;
        return this;
    }

    public Child addTutor(Tutor tutor) {
        this.tutors.add(tutor);
        tutor.getChildren().add(this);
        return this;
    }

    public Child removeTutor(Tutor tutor) {
        this.tutors.remove(tutor);
        tutor.getChildren().remove(this);
        return this;
    }

    public void setTutors(Set<Tutor> tutors) {
        this.tutors = tutors;
    }

    public Set<Organization> getOrganizations() {
        return organizations;
    }

    public Child organizations(Set<Organization> organizations) {
        this.organizations = organizations;
        return this;
    }

    public Child addOrganization(Organization organization) {
        this.organizations.add(organization);
        return this;
    }

    public Child removeOrganization(Organization organization) {
        this.organizations.remove(organization);
        return this;
    }

    public void setOrganizations(Set<Organization> organizations) {
        this.organizations = organizations;
    }

    public DocumentFile getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(DocumentFile photoFile) {
        this.photoFile = photoFile;
    }

    public Child photoFile(DocumentFile photoFile) {
        this.photoFile = photoFile;
        return this;
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
        Child child = (Child) o;
        if (child.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), child.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Child{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nifCountry='" + getNifCountry() + "'" +
            ", nifNumber='" + getNifNumber() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", famility='" + isFamility() + "'" +
            ", statusType='" + getStatusType() + "'" +
            "}";
    }
}
