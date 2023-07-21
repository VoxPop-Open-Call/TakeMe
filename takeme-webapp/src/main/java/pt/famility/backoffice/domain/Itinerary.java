package pt.famility.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import pt.famility.backoffice.domain.enumeration.ItineraryStatusType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Itinerary.
 */
@Entity
@Table(name = "itinerary")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Itinerary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "scheduled_time", nullable = false)
    private Instant scheduledTime;

    @Column(name = "effective_start_time")
    private Instant effectiveStartTime;

    @Column(name = "effective_end_time")
    private Instant effectiveEndTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "itinerary_status_type", nullable = false)
    private ItineraryStatusType itineraryStatusType;

    @NotNull
    @Column(name = "estimated_km", nullable = false)
    private Double estimatedKM;

    @Column(name = "estimated_total_time")
    private Integer estimatedTotalTime;

    @Column(name = "ocupation")
    private Double ocupation;

    /**
     * This is the start location given when creating the itinerary object in the Backoffice
     */
    @ManyToOne
    @JoinColumn(name = "estimated_start_location")
    @JsonIgnoreProperties("")
    private Location estimatedStartLocation;

    /**
     * This is the effective start location given by the driver when he starts the itinerary;
     * When the itinerary is created, it is set to null
     */
    @ManyToOne
    @JoinColumn(name = "start_location")
    @JsonIgnoreProperties("")
    private Location startLocation;

    @ManyToOne
    @JoinColumn(name = "end_location")
    @JsonIgnoreProperties("")
    private Location endLocation;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Organization organization;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Vehicle vehicle;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Driver driver;

    @ManyToOne
    private PromoterItinerary promoterItinerary;

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

    public Itinerary name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Instant scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Instant getEffectiveStartTime() {
        return effectiveStartTime;
    }

    public Itinerary effectiveStartTime(Instant effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
        return this;
    }

    public void setEffectiveStartTime(Instant effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }

    public Instant getEffectiveEndTime() {
        return effectiveEndTime;
    }

    public Itinerary effectiveEndTime(Instant effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
        return this;
    }

    public void setEffectiveEndTime(Instant effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }

    public ItineraryStatusType getItineraryStatusType() {
        return itineraryStatusType;
    }

    public Itinerary itineraryStatusType(ItineraryStatusType itineraryStatusType) {
        this.itineraryStatusType = itineraryStatusType;
        return this;
    }

    public void setItineraryStatusType(ItineraryStatusType itineraryStatusType) {
        this.itineraryStatusType = itineraryStatusType;
    }

    public Double getEstimatedKM() {
        return estimatedKM;
    }

    public Itinerary estimatedKM(Double estimatedKM) {
        this.estimatedKM = estimatedKM;
        return this;
    }

    public void setEstimatedKM(Double estimatedKM) {
        this.estimatedKM = estimatedKM;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Itinerary organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Itinerary vehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Driver getDriver() {
        return driver;
    }

    public Itinerary driver(Driver driver) {
        this.driver = driver;
        return this;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Integer getEstimatedTotalTime() {
        return estimatedTotalTime;
    }

    public void setEstimatedTotalTime(Integer estimatedTotalTime) {
        this.estimatedTotalTime = estimatedTotalTime;
    }

    public Double getOcupation() {
        return ocupation;
    }

    public void setOcupation(Double ocupation) {
        this.ocupation = ocupation;
    }

    public Location getEstimatedStartLocation() {
        return estimatedStartLocation;
    }

    public void setEstimatedStartLocation(Location estimatedStartLocation) {
        this.estimatedStartLocation = estimatedStartLocation;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public Itinerary estimatedTotalTime(Integer estimatedTotalTime) {
        this.estimatedTotalTime = estimatedTotalTime;
        return this;
    }

    public Itinerary ocupation(Double ocupation) {
        this.ocupation = ocupation;
        return this;
    }

    public Itinerary estimatedStartLocation(Location estimatedStartLocation) {
        this.estimatedStartLocation = estimatedStartLocation;
        return this;
    }

    public Itinerary startLocation(Location startLocation) {
        this.startLocation = startLocation;
        return this;
    }

    public Itinerary endLocation(Location endLocation) {
        this.endLocation = endLocation;
        return this;
    }

    public PromoterItinerary getPromoterItinerary() {
        return promoterItinerary;
    }

    public Itinerary promoterItinerary(PromoterItinerary promoterItinerary) {
        this.promoterItinerary = promoterItinerary;
        return this;
    }

    public void setPromoterItinerary(PromoterItinerary promoterItinerary) {
        this.promoterItinerary = promoterItinerary;
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
        Itinerary itinerary = (Itinerary) o;
        if (itinerary.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), itinerary.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Itinerary{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", scheduledTime='" + getScheduledTime() + "'" +
            ", effectiveStartTime='" + getEffectiveStartTime() + "'" +
            ", effectiveEndTime='" + getEffectiveEndTime() + "'" +
            ", itineraryStatusType='" + getItineraryStatusType() + "'" +
            ", estimatedKM=" + getEstimatedKM() +
            "}";
    }
}
