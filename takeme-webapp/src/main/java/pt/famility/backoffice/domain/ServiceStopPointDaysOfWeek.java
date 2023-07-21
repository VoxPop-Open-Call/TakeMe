package pt.famility.backoffice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

import pt.famility.backoffice.domain.enumeration.DaysOfWeekType;

/**
 * A ServiceStopPointDaysOfWeek.
 */
@Entity
@Table(name = "svc_sp_dow")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ServiceStopPointDaysOfWeek implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day")
    private DaysOfWeekType day;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DaysOfWeekType getDay() {
        return day;
    }

    public ServiceStopPointDaysOfWeek day(DaysOfWeekType day) {
        this.day = day;
        return this;
    }

    public void setDay(DaysOfWeekType day) {
        this.day = day;
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
        ServiceStopPointDaysOfWeek serviceStopPointDaysOfWeek = (ServiceStopPointDaysOfWeek) o;
        if (serviceStopPointDaysOfWeek.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceStopPointDaysOfWeek.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceStopPointDaysOfWeek{" +
            "id=" + getId() +
            ", day='" + getDay() + "'" +
            "}";
    }
}
