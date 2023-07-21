package pt.famility.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A Service.
 */
@Entity
@Table(name = "svc")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Service implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "recurrent", nullable = false)
    private Boolean recurrent;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Organization organization;

    @ManyToOne
    @JsonIgnoreProperties("")
    private ChildItinerarySubscription childItinerarySubscription;

    @OneToMany(mappedBy = "service", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("")
    @JsonManagedReference
    private List<ServiceStopPoint> serviceStopPoints;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isRecurrent() {
        return recurrent;
    }

    public Service recurrent(Boolean recurrent) {
        this.recurrent = recurrent;
        return this;
    }

    public void setRecurrent(Boolean recurrent) {
        this.recurrent = recurrent;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Service startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public Service endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Service organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public ChildItinerarySubscription getChildItinerarySubscription() {
        return childItinerarySubscription;
    }

    public Service childItinerarySubscription(ChildItinerarySubscription childItinerarySubscription) {
        this.childItinerarySubscription = childItinerarySubscription;
        return this;
    }

    public void setChildItinerarySubscription(ChildItinerarySubscription childItinerarySubscription) {
        this.childItinerarySubscription = childItinerarySubscription;
    }

    public List<ServiceStopPoint> getServiceStopPoints() {
        return serviceStopPoints;
    }

    public void setServiceStopPoints(List<ServiceStopPoint> serviceStopPoints) {
        this.serviceStopPoints = serviceStopPoints;
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
        Service service = (Service) o;
        if (service.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), service.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + getId() +
                ", recurrent='" + isRecurrent() + "'" +
                ", startDate='" + getStartDate() + "'" +
                ", endDate='" + getEndDate() + "'" +
                "}";
    }
}
