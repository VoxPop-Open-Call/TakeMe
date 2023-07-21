package pt.famility.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import pt.famility.backoffice.domain.enumeration.StopEventType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A StopAuditEvent.
 */
@Entity
@Table(name = "stop_audit_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StopAuditEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "event_time", nullable = false)
    private Instant eventTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private StopEventType eventType;

    @ManyToOne
    @JsonIgnore
    private StopPoint stopPoint;

    @OneToOne
    private Child child;

    @ManyToOne
    @JsonIgnore
    private Itinerary itinerary;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEventTime() {
        return eventTime;
    }

    public StopAuditEvent eventTime(Instant eventTime) {
        this.eventTime = eventTime;
        return this;
    }

    public void setEventTime(Instant eventTime) {
        this.eventTime = eventTime;
    }

    public StopEventType getEventType() {
        return eventType;
    }

    public StopAuditEvent eventType(StopEventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public void setEventType(StopEventType eventType) {
        this.eventType = eventType;
    }

    public StopPoint getStopPoint() {
        return stopPoint;
    }

    public StopAuditEvent stopPoint(StopPoint stopPoint) {
        this.stopPoint = stopPoint;
        return this;
    }

    public void setStopPoint(StopPoint stopPoint) {
        this.stopPoint = stopPoint;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public StopAuditEvent child(Child child) {
        this.child = child;
        return this;
    }

    public StopAuditEvent itinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
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
        StopAuditEvent stopAuditEvent = (StopAuditEvent) o;
        if (stopAuditEvent.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stopAuditEvent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StopAuditEvent{" +
            "id=" + getId() +
            ", eventTime='" + getEventTime() + "'" +
            ", eventType='" + getEventType() + "'" +
            "}";
    }
}
