package pt.famility.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import pt.famility.backoffice.domain.enumeration.OriginType;

/**
 * A UserMessagingToken.
 */
@Entity
@Table(name = "user_messaging_token")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserMessagingToken extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "token", nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "origin")
    private OriginType origin;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public UserMessagingToken token(String token) {
        this.token = token;
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public OriginType getOrigin() {
        return origin;
    }

    public UserMessagingToken origin(OriginType origin) {
        this.origin = origin;
        return this;
    }

    public void setOrigin(OriginType origin) {
        this.origin = origin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserMessagingToken user(User user) {
        this.user = user;
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
        UserMessagingToken userMessagingToken = (UserMessagingToken) o;
        if (userMessagingToken.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userMessagingToken.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserMessagingToken{" +
            "id=" + getId() +
            ", token='" + getToken() + "'" +
            ", origin='" + getOrigin() + "'" +
            "}";
    }
}
