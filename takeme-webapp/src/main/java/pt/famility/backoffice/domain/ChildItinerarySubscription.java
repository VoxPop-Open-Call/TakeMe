package pt.famility.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import pt.famility.backoffice.domain.enumeration.StatusType;

/**
 * A ChildItinerarySubscription.
 */
@Entity
@Table(name = "child_itinerary_subscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChildItinerarySubscription extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_type", nullable = false)
    private StatusType statusType;

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

    @ManyToOne(optional = false)
    @NotNull
    private Child child;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "service", "organization" }, allowSetters = true)
    private PromoterItinerary promoterItinerary;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChildItinerarySubscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusType getStatusType() {
        return this.statusType;
    }

    public ChildItinerarySubscription statusType(StatusType statusType) {
        this.setStatusType(statusType);
        return this;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public Instant getSubscriptionDate() {
        return this.subscriptionDate;
    }

    public ChildItinerarySubscription subscriptionDate(Instant subscriptionDate) {
        this.setSubscriptionDate(subscriptionDate);
        return this;
    }

    public void setSubscriptionDate(Instant subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public Instant getActivationDate() {
        return this.activationDate;
    }

    public ChildItinerarySubscription activationDate(Instant activationDate) {
        this.setActivationDate(activationDate);
        return this;
    }

    public void setActivationDate(Instant activationDate) {
        this.activationDate = activationDate;
    }

    public Instant getDeactivationDate() {
        return this.deactivationDate;
    }

    public ChildItinerarySubscription deactivationDate(Instant deactivationDate) {
        this.setDeactivationDate(deactivationDate);
        return this;
    }

    public void setDeactivationDate(Instant deactivationDate) {
        this.deactivationDate = deactivationDate;
    }

    public String getComments() {
        return this.comments;
    }

    public ChildItinerarySubscription comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAdditionalInformation() {
        return this.additionalInformation;
    }

    public ChildItinerarySubscription additionalInformation(String additionalInformation) {
        this.setAdditionalInformation(additionalInformation);
        return this;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public ChildItinerarySubscription cardNumber(String cardNumber) {
        this.setCardNumber(cardNumber);
        return this;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Child getChild() {
        return this.child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public ChildItinerarySubscription child(Child child) {
        this.setChild(child);
        return this;
    }

    public PromoterItinerary getPromoterItinerary() {
        return this.promoterItinerary;
    }

    public void setPromoterItinerary(PromoterItinerary promoterItinerary) {
        this.promoterItinerary = promoterItinerary;
    }

    public ChildItinerarySubscription promoterItinerary(PromoterItinerary promoterItinerary) {
        this.setPromoterItinerary(promoterItinerary);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ChildItinerarySubscription user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChildItinerarySubscription)) {
            return false;
        }
        return id != null && id.equals(((ChildItinerarySubscription) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChildItinerarySubscription{" +
            "id=" + getId() +
            ", statusType='" + getStatusType() + "'" +
            ", subscriptionDate='" + getSubscriptionDate() + "'" +
            ", activationDate='" + getActivationDate() + "'" +
            ", deactivationDate='" + getDeactivationDate() + "'" +
            ", comments='" + getComments() + "'" +
            ", additionalInformation='" + getAdditionalInformation() + "'" +
            ", cardNumber='" + getCardNumber() + "'" +
            "}";
    }
}
