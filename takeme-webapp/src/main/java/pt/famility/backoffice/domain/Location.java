package pt.famility.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import pt.famility.backoffice.domain.enumeration.LocationType;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "designation", nullable = false)
    private String designation;

    @NotNull
    @Column(name = "street", nullable = false)
    private String street;

    @NotNull
    @Column(name = "port_number", nullable = false)
    private String portNumber;

    @Column(name = "floor")
    private String floor;

    @NotNull
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private LocationType type;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private String longitude;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private String latitude;

    @Column(name = "plus_code")
    private String plusCode;

    @ManyToMany(mappedBy = "locations")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "identificationCardType", "locations", "children" }, allowSetters = true)
    private Set<Tutor> tutors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public Location designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getStreet() {
        return street;
    }

    public Location street(String street) {
        this.street = street;
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public Location portNumber(String portNumber) {
        this.portNumber = portNumber;
        return this;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public String getFloor() {
        return floor;
    }

    public Location floor(String floor) {
        this.floor = floor;
        return this;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Location postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public Location city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public Location country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocationType getType() {
        return type;
    }

    public Location type(LocationType type) {
        this.type = type;
        return this;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public String getLongitude() {
        return longitude;
    }

    public Location longitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public Location latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPlusCode() {
        return plusCode;
    }

    public Location plusCode(String plusCode) {
        this.plusCode = plusCode;
        return this;
    }

    public void setPlusCode(String plusCode) {
        this.plusCode = plusCode;
    }

    public Set<Tutor> getTutors() {
        return this.tutors;
    }

    public void setTutors(Set<Tutor> tutors) {
        if (this.tutors != null) {
            this.tutors.forEach(i -> i.removeLocation(this));
        }
        if (tutors != null) {
            tutors.forEach(i -> i.addLocation(this));
        }
        this.tutors = tutors;
    }

    public Location tutors(Set<Tutor> tutors) {
        this.setTutors(tutors);
        return this;
    }

    public Location addTutor(Tutor tutor) {
        this.tutors.add(tutor);
        tutor.getLocations().add(this);
        return this;
    }

    public Location removeTutor(Tutor tutor) {
        this.tutors.remove(tutor);
        tutor.getLocations().remove(this);
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
        Location location = (Location) o;
        if (location.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), location.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", street='" + getStreet() + "'" +
            ", portNumber='" + getPortNumber() + "'" +
            ", floor='" + getFloor() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", type='" + getType() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", plusCode='" + getPlusCode() + "'" +
            "}";
    }
}
