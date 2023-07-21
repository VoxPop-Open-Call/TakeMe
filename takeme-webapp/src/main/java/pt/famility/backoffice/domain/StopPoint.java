package pt.famility.backoffice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import pt.famility.backoffice.domain.enumeration.StopPointType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A StopPoint.
 */
@Entity
@Table(name = "stop_point")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StopPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "stop_point_type", nullable = false)
    private StopPointType stopPointType;

    @NotNull
    @Column(name = "scheduled_time", nullable = false)
    private Instant scheduledTime;

    @Column(name = "estimated_arrive_time")
    private Instant estimatedArriveTime;

    @Column(name = "effective_arrive_time", nullable = true)
    private Instant effectiveArriveTime;

    @OneToOne
    @JoinColumn()
    private Location location;

    @ManyToMany()
    @JoinTable(name = "stop_point_child",
        joinColumns = @JoinColumn(name = "stop_point_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "child_id", referencedColumnName = "id"))
    private List<Child> childList = new ArrayList<>();

    @OneToMany(mappedBy = "stopPoint")
    private Set<StopAuditEvent> stopAuditEvents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StopPointType getStopPointType() {
        return stopPointType;
    }

    public StopPoint stopPointType(StopPointType stopPointType) {
        this.stopPointType = stopPointType;
        return this;
    }

    public void setStopPointType(StopPointType stopPointType) {
        this.stopPointType = stopPointType;
    }

    public Instant getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Instant scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Location getLocation() {
        return location;
    }

    public StopPoint location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }

    public Set<StopAuditEvent> getStopAuditEvents() {
        return stopAuditEvents;
    }

    public StopPoint stopAuditEvents(Set<StopAuditEvent> stopAuditEvent) {
        this.stopAuditEvents = stopAuditEvent;
        return this;
    }

    public Instant getEstimatedArriveTime() {
        return estimatedArriveTime;
    }

    public void setEstimatedArriveTime(Instant estimatedArriveTime) {
        this.estimatedArriveTime = estimatedArriveTime;
    }

    public StopPoint estimatedArriveTime(Instant estimatedArriveTime) {
        this.estimatedArriveTime = estimatedArriveTime;
        return this;
    }

    public void setStopAuditEvents(Set<StopAuditEvent> stopAuditEvents) {
        this.stopAuditEvents = stopAuditEvents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public void addChild(Child child) {
        childList.add(child);
    }

    public void removeChild(Child child) {
        childList.remove(child);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StopPoint stopPoint = (StopPoint) o;
        if (stopPoint.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stopPoint.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StopPoint{" +
            "id=" + getId() +
            ", stopPointType='" + getStopPointType() + "'" +
            ", scheduledTime='" + getScheduledTime() + "'" +
            "}";
    }

    public Instant getEffectiveArriveTime() {
        return effectiveArriveTime;
    }

    public void setEffectiveArriveTime(Instant effectiveArriveTime) {
        this.effectiveArriveTime = effectiveArriveTime;
    }
}
