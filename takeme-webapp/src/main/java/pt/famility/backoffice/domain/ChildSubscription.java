package pt.famility.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import pt.famility.backoffice.domain.enumeration.StatusType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * A ChildSubscription.
 */
@Entity
@Table(name = "child_subscription")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChildSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_type", nullable = false)
    private StatusType statusType;

    @Column(name = "famility")
    private Boolean famility;

    @NotNull
    @Column(name = "subscription_date", nullable = false)
    private Instant subscriptionDate;

    @Column(name = "activation_date")
    private Instant activationDate;

    @Column(name = "deactivation_date")
    private Instant deactivationDate;

    @Column(name = "comments")
    private String comments;

    @Column(name = "additional_information")
    private String additionalInformation;

    @Column(name = "card_number")
    private String cardNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties("")
    private Child child;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Organization organization;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties("")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public ChildSubscription statusType(StatusType statusType) {
        this.statusType = statusType;
        return this;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public Boolean isFamility() {
        return famility;
    }

    public ChildSubscription famility(Boolean famility) {
        this.famility = famility;
        return this;
    }

    public void setFamility(Boolean famility) {
        this.famility = famility;
    }

    public Instant getSubscriptionDate() {
        return subscriptionDate;
    }

    public ChildSubscription subscriptionDate(Instant subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
        return this;
    }

    public void setSubscriptionDate(Instant subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public Instant getActivationDate() {
        return activationDate;
    }

    public ChildSubscription activationDate(Instant activationDate) {
        this.activationDate = activationDate;
        return this;
    }

    public void setActivationDate(Instant activationDate) {
        this.activationDate = activationDate;
    }

    public Instant getDeactivationDate() {
        return deactivationDate;
    }

    public void setDeactivationDate(Instant deactivationDate) {
        this.deactivationDate = deactivationDate;
    }

    public ChildSubscription deactivationDate(Instant deactivationDate) {
        this.deactivationDate = deactivationDate;
        return this;
    }

    public String getComments() {
        return comments;
    }

    public ChildSubscription comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public ChildSubscription additionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
        return this;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Child getChild() {
        return child;
    }

    public ChildSubscription child(Child child) {
        this.child = child;
        return this;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Organization getOrganization() {
        return organization;
    }

    public ChildSubscription organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public ChildSubscription user(User user) {
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
        ChildSubscription childSubscription = (ChildSubscription) o;
        if (childSubscription.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), childSubscription.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChildSubscription{" +
            "id=" + getId() +
            ", statusType='" + getStatusType() + "'" +
            ", famility='" + isFamility() + "'" +
            ", subscriptionDate='" + getSubscriptionDate() + "'" +
            ", activationDate='" + getActivationDate() + "'" +
            ", comments='" + getComments() + "'" +
            ", additionalInformation='" + getAdditionalInformation() + "'" +
            "}";
    }
}
