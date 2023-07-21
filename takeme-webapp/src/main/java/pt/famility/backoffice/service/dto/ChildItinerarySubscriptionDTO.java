package pt.famility.backoffice.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;
import pt.famility.backoffice.domain.enumeration.StatusType;

/**
 * A DTO for the {@link pt.famility.backoffice.domain.ChildItinerarySubscription} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChildItinerarySubscriptionDTO implements Serializable {

    private Long id;

    @NotNull
    private StatusType statusType;

    @NotNull
    private Instant subscriptionDate;

    private Instant activationDate;

    private Instant deactivationDate;

    private String comments;

    private String additionalInformation;

    private String cardNumber;

    private ChildDTO child;

    private PromoterItineraryDTO promoterItinerary;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public Instant getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(Instant subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public Instant getActivationDate() {
        return activationDate;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
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

    public ChildDTO getChild() {
        return child;
    }

    public void setChild(ChildDTO child) {
        this.child = child;
    }

    public PromoterItineraryDTO getPromoterItinerary() {
        return promoterItinerary;
    }

    public void setPromoterItinerary(PromoterItineraryDTO promoterItinerary) {
        this.promoterItinerary = promoterItinerary;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChildItinerarySubscriptionDTO)) {
            return false;
        }

        ChildItinerarySubscriptionDTO childItinerarySubscriptionDTO = (ChildItinerarySubscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, childItinerarySubscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChildItinerarySubscriptionDTO{" +
            "id=" + getId() +
            ", statusType='" + getStatusType() + "'" +
            ", subscriptionDate='" + getSubscriptionDate() + "'" +
            ", activationDate='" + getActivationDate() + "'" +
            ", deactivationDate='" + getDeactivationDate() + "'" +
            ", comments='" + getComments() + "'" +
            ", additionalInformation='" + getAdditionalInformation() + "'" +
            ", cardNumber='" + getCardNumber() + "'" +
            ", child=" + getChild() +
            ", promoterItinerary=" + getPromoterItinerary() +
            ", user=" + getUser() +
            "}";
    }
}
