package pt.famility.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PromoterStopPoint.
 */
@Entity
@Table(name = "promoter_stop_point")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PromoterStopPoint extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "scheduled_time", nullable = false)
    private Instant scheduledTime;

    @ManyToOne
    @JsonIgnoreProperties(value = { "service" }, allowSetters = true)
    private PromoterItinerary promoterItinerary;

    @ManyToOne
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PromoterStopPoint id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PromoterStopPoint name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getScheduledTime() {
        return this.scheduledTime;
    }

    public PromoterStopPoint scheduledTime(Instant scheduledTime) {
        this.setScheduledTime(scheduledTime);
        return this;
    }

    public void setScheduledTime(Instant scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public PromoterItinerary getPromoterItinerary() {
        return this.promoterItinerary;
    }

    public void setPromoterItinerary(PromoterItinerary promoterItinerary) {
        this.promoterItinerary = promoterItinerary;
    }

    public PromoterStopPoint promoterItinerary(PromoterItinerary promoterItinerary) {
        this.setPromoterItinerary(promoterItinerary);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public PromoterStopPoint location(Location location) {
        this.setLocation(location);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromoterStopPoint)) {
            return false;
        }
        return id != null && id.equals(((PromoterStopPoint) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromoterStopPoint{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", scheduledTime='" + getScheduledTime() + "'" +
            "}";
    }
}
