package pt.famility.backoffice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

import pt.famility.backoffice.domain.enumeration.NotificationTypeType;

/**
 * A NotificationType.
 */
@Entity
@Table(name = "notification_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NotificationType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private NotificationTypeType type;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "title")
    private String title;

    @Column(name = "user_configurable")
    private Boolean userConfigurable;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationTypeType getType() {
        return type;
    }

    public NotificationType type(NotificationTypeType type) {
        this.type = type;
        return this;
    }

    public void setType(NotificationTypeType type) {
        this.type = type;
    }

    public Boolean isActive() {
        return active;
    }

    public NotificationType active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public NotificationType title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isUserConfigurable() {
        return userConfigurable;
    }

    public NotificationType userConfigurable(Boolean userConfigurable) {
        this.userConfigurable = userConfigurable;
        return this;
    }

    public void setUserConfigurable(Boolean userConfigurable) {
        this.userConfigurable = userConfigurable;
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
        NotificationType notificationType = (NotificationType) o;
        if (notificationType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificationType{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", active='" + isActive() + "'" +
            ", title='" + getTitle() + "'" +
            ", userConfigurable='" + isUserConfigurable() + "'" +
            "}";
    }
}
