package pt.famility.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import pt.famility.backoffice.domain.enumeration.StatusType;
import pt.famility.backoffice.domain.enumeration.StopPointType;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A ServiceStopPoint.
 */
@Entity
@Table(name = "svc_sp")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ServiceStopPoint implements Serializable {

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
    @Column(name = "start_hour", nullable = false)
    private String startHour;

    @NotNull
    @Column(name = "combined_time", nullable = false)
    private String combinedTime;

    @NotNull
    @Column(name = "start_frequency_date", nullable = false)
    private Instant startFrequencyDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_type", nullable = false)
    private StatusType statusType;

    @ManyToOne
    private ServiceStopPointFrequency frequency;

    @OneToOne    @JoinColumn(unique = true)
    private Location location;

    @ManyToOne
    @JsonBackReference
    private Service service;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "svc_sp_svc_sp_dow",
               joinColumns = @JoinColumn(name = "service_stop_points_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "service_stop_point_days_of_weeks_id", referencedColumnName = "id"))
    private Set<ServiceStopPointDaysOfWeek> serviceStopPointDaysOfWeeks = new HashSet<>();

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

    public ServiceStopPoint stopPointType(StopPointType stopPointType) {
        this.stopPointType = stopPointType;
        return this;
    }

    public void setStopPointType(StopPointType stopPointType) {
        this.stopPointType = stopPointType;
    }

    public String getStartHour() {
        return startHour;
    }

    public ServiceStopPoint startHour(String startHour) {
        this.startHour = startHour;
        return this;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getCombinedTime() {
        return combinedTime;
    }

    public ServiceStopPoint combinedTime(String combinedTime) {
        this.combinedTime = combinedTime;
        return this;
    }

    public void setCombinedTime(String combinedTime) {
        this.combinedTime = combinedTime;
    }

    public Instant getStartFrequencyDate() {
        return startFrequencyDate;
    }

    public ServiceStopPoint startFrequencyDate(Instant startFrequencyDate) {
        this.startFrequencyDate = startFrequencyDate;
        return this;
    }

    public void setStartFrequencyDate(Instant startFrequencyDate) {
        this.startFrequencyDate = startFrequencyDate;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public ServiceStopPoint statusType(StatusType statusType) {
        this.statusType = statusType;
        return this;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public ServiceStopPointFrequency getFrequency() {
        return frequency;
    }

    public ServiceStopPoint frequency(ServiceStopPointFrequency serviceStopPointFrequency) {
        this.frequency = serviceStopPointFrequency;
        return this;
    }

    public void setFrequency(ServiceStopPointFrequency serviceStopPointFrequency) {
        this.frequency = serviceStopPointFrequency;
    }

    public Set<ServiceStopPointDaysOfWeek> getServiceStopPointDaysOfWeeks() {
        return serviceStopPointDaysOfWeeks;
    }

    public ServiceStopPoint serviceStopPointDaysOfWeeks(Set<ServiceStopPointDaysOfWeek> serviceStopPointDaysOfWeeks) {
        this.serviceStopPointDaysOfWeeks = serviceStopPointDaysOfWeeks;
        return this;
    }

    public ServiceStopPoint addServiceStopPointDaysOfWeek(ServiceStopPointDaysOfWeek serviceStopPointDaysOfWeek) {
        this.serviceStopPointDaysOfWeeks.add(serviceStopPointDaysOfWeek);
        return this;
    }

    public ServiceStopPoint removeServiceStopPointDaysOfWeek(ServiceStopPointDaysOfWeek serviceStopPointDaysOfWeek) {
        this.serviceStopPointDaysOfWeeks.remove(serviceStopPointDaysOfWeek);
        return this;
    }

    public void setServiceStopPointDaysOfWeeks(Set<ServiceStopPointDaysOfWeek> serviceStopPointDaysOfWeeks) {
        this.serviceStopPointDaysOfWeeks = serviceStopPointDaysOfWeeks;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public ServiceStopPoint location(Location location) {
        this.location = location;
        return this;
    }

    public ServiceStopPoint service(Service service) {
        this.service = service;
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
        ServiceStopPoint serviceStopPoint = (ServiceStopPoint) o;
        if (serviceStopPoint.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceStopPoint.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceStopPoint{" +
            "id=" + getId() +
            ", stopPointType='" + getStopPointType() + "'" +
            ", startHour='" + getStartHour() + "'" +
            ", combinedTime='" + getCombinedTime() + "'" +
            ", startFrequencyDate='" + getStartFrequencyDate() + "'" +
            ", statusType='" + getStatusType() + "'" +
            "}";
    }
}
