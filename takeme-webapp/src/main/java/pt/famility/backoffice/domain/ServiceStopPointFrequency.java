package pt.famility.backoffice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ServiceStopPointFrequency.
 */
@Entity
@Table(name = "svc_sp_freq")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ServiceStopPointFrequency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "week_interval", nullable = false)
    private Integer weekInterval;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public ServiceStopPointFrequency description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWeekInterval() {
        return weekInterval;
    }

    public ServiceStopPointFrequency weekInterval(Integer weekInterval) {
        this.weekInterval = weekInterval;
        return this;
    }

    public void setWeekInterval(Integer weekInterval) {
        this.weekInterval = weekInterval;
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
        ServiceStopPointFrequency serviceStopPointFrequency = (ServiceStopPointFrequency) o;
        if (serviceStopPointFrequency.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceStopPointFrequency.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceStopPointFrequency{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", weekInterval=" + getWeekInterval() +
            "}";
    }
}
