package pt.famility.backoffice.service.criteria;

import org.springdoc.api.annotations.ParameterObject;
import pt.famility.backoffice.domain.enumeration.StatusType;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link pt.famility.backoffice.domain.ChildItinerarySubscription} entity. This class is used
 * in {@link pt.famility.backoffice.web.rest.ChildItinerarySubscriptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /child-itinerary-subscriptions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChildItinerarySubscriptionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatusType
     */
    public static class StatusTypeFilter extends Filter<StatusType> {

        public StatusTypeFilter() {
        }

        public StatusTypeFilter(StatusTypeFilter filter) {
            super(filter);
        }

        @Override
        public StatusTypeFilter copy() {
            return new StatusTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StatusTypeFilter statusType;

    private InstantFilter subscriptionDate;

    private InstantFilter activationDate;

    private InstantFilter deactivationDate;

    private StringFilter comments;

    private StringFilter additionalInformation;

    private StringFilter cardNumber;

    private LongFilter childId;

    private StringFilter childName;

    private LongFilter promoterItineraryId;

    private StringFilter promoterItineraryName;

    private LongFilter organizationId;

    private LongFilter promoterServiceId;

    private LongFilter userId;

    private Boolean distinct;

    public ChildItinerarySubscriptionCriteria() {
    }

    public ChildItinerarySubscriptionCriteria(ChildItinerarySubscriptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.statusType = other.statusType == null ? null : other.statusType.copy();
        this.subscriptionDate = other.subscriptionDate == null ? null : other.subscriptionDate.copy();
        this.activationDate = other.activationDate == null ? null : other.activationDate.copy();
        this.deactivationDate = other.deactivationDate == null ? null : other.deactivationDate.copy();
        this.comments = other.comments == null ? null : other.comments.copy();
        this.additionalInformation = other.additionalInformation == null ? null : other.additionalInformation.copy();
        this.cardNumber = other.cardNumber == null ? null : other.cardNumber.copy();
        this.childId = other.childId == null ? null : other.childId.copy();
        this.childName = other.childName == null ? null : other.childName.copy();
        this.promoterItineraryId = other.promoterItineraryId == null ? null : other.promoterItineraryId.copy();
        this.promoterItineraryName = other.promoterItineraryName == null ? null : other.promoterItineraryName.copy();
        this.organizationId = other.organizationId == null ? null : other.organizationId.copy();
        this.promoterServiceId = other.promoterServiceId == null ? null : other.promoterServiceId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ChildItinerarySubscriptionCriteria copy() {
        return new ChildItinerarySubscriptionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StatusTypeFilter getStatusType() {
        return statusType;
    }

    public StatusTypeFilter statusType() {
        if (statusType == null) {
            statusType = new StatusTypeFilter();
        }
        return statusType;
    }

    public void setStatusType(StatusTypeFilter statusType) {
        this.statusType = statusType;
    }

    public InstantFilter getSubscriptionDate() {
        return subscriptionDate;
    }

    public InstantFilter subscriptionDate() {
        if (subscriptionDate == null) {
            subscriptionDate = new InstantFilter();
        }
        return subscriptionDate;
    }

    public void setSubscriptionDate(InstantFilter subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public InstantFilter getActivationDate() {
        return activationDate;
    }

    public InstantFilter activationDate() {
        if (activationDate == null) {
            activationDate = new InstantFilter();
        }
        return activationDate;
    }

    public void setActivationDate(InstantFilter activationDate) {
        this.activationDate = activationDate;
    }

    public InstantFilter getDeactivationDate() {
        return deactivationDate;
    }

    public InstantFilter deactivationDate() {
        if (deactivationDate == null) {
            deactivationDate = new InstantFilter();
        }
        return deactivationDate;
    }

    public void setDeactivationDate(InstantFilter deactivationDate) {
        this.deactivationDate = deactivationDate;
    }

    public StringFilter getComments() {
        return comments;
    }

    public StringFilter comments() {
        if (comments == null) {
            comments = new StringFilter();
        }
        return comments;
    }

    public void setComments(StringFilter comments) {
        this.comments = comments;
    }

    public StringFilter getAdditionalInformation() {
        return additionalInformation;
    }

    public StringFilter additionalInformation() {
        if (additionalInformation == null) {
            additionalInformation = new StringFilter();
        }
        return additionalInformation;
    }

    public void setAdditionalInformation(StringFilter additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public StringFilter getCardNumber() {
        return cardNumber;
    }

    public StringFilter cardNumber() {
        if (cardNumber == null) {
            cardNumber = new StringFilter();
        }
        return cardNumber;
    }

    public void setCardNumber(StringFilter cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LongFilter getChildId() {
        return childId;
    }

    public LongFilter childId() {
        if (childId == null) {
            childId = new LongFilter();
        }
        return childId;
    }

    public void setChildId(LongFilter childId) {
        this.childId = childId;
    }

    public StringFilter getChildName() {
        return childName;
    }

    public StringFilter childName() {
        if (childName == null) {
            childName = new StringFilter();
        }
        return childName;
    }

    public void setChildName(StringFilter childName) {
        this.childName = childName;
    }

    public LongFilter getPromoterItineraryId() {
        return promoterItineraryId;
    }

    public LongFilter promoterItineraryId() {
        if (promoterItineraryId == null) {
            promoterItineraryId = new LongFilter();
        }
        return promoterItineraryId;
    }

    public void setPromoterItineraryId(LongFilter promoterItineraryId) {
        this.promoterItineraryId = promoterItineraryId;
    }

    public StringFilter getPromoterItineraryName() {
        return promoterItineraryName;
    }

    public StringFilter promoterItineraryName() {
        if (promoterItineraryName == null) {
            promoterItineraryName = new StringFilter();
        }
        return promoterItineraryName;
    }

    public void setPromoterItineraryName(StringFilter promoterItineraryName) {
        this.promoterItineraryName = promoterItineraryName;
    }

    public LongFilter getOrganizationId() {
        return organizationId;
    }

    public LongFilter organizationId() {
        if (organizationId == null) {
            organizationId = new LongFilter();
        }
        return organizationId;
    }

    public void setOrganizationId(LongFilter promoterItineraryOrganizationId) {
        this.organizationId = promoterItineraryOrganizationId;
    }

    public LongFilter getPromoterServiceId() {
        return promoterServiceId;
    }

    public LongFilter promoterServiceId() {
        if (promoterServiceId == null) {
            promoterServiceId = new LongFilter();
        }
        return promoterServiceId;
    }

    public void setPromoterServiceId(LongFilter promoterServiceId) {
        this.promoterServiceId = promoterServiceId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ChildItinerarySubscriptionCriteria that = (ChildItinerarySubscriptionCriteria) o;
        return (
                Objects.equals(id, that.id) &&
                        Objects.equals(statusType, that.statusType) &&
                        Objects.equals(subscriptionDate, that.subscriptionDate) &&
                        Objects.equals(activationDate, that.activationDate) &&
                        Objects.equals(deactivationDate, that.deactivationDate) &&
                        Objects.equals(comments, that.comments) &&
                        Objects.equals(additionalInformation, that.additionalInformation) &&
                        Objects.equals(cardNumber, that.cardNumber) &&
                        Objects.equals(childId, that.childId) &&
                        Objects.equals(childName, that.childName) &&
                        Objects.equals(promoterItineraryId, that.promoterItineraryId) &&
                        Objects.equals(promoterItineraryName, that.promoterItineraryName) &&
                        Objects.equals(organizationId, that.organizationId) &&
                        Objects.equals(promoterServiceId, that.promoterServiceId) &&
                        Objects.equals(userId, that.userId) &&
                        Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                statusType,
                subscriptionDate,
                activationDate,
                deactivationDate,
                comments,
                additionalInformation,
                cardNumber,
                childId,
                childName,
                promoterItineraryId,
                promoterItineraryName,
                organizationId,
                promoterServiceId,
                userId,
                distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChildItinerarySubscriptionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (statusType != null ? "statusType=" + statusType + ", " : "") +
                (subscriptionDate != null ? "subscriptionDate=" + subscriptionDate + ", " : "") +
                (activationDate != null ? "activationDate=" + activationDate + ", " : "") +
                (deactivationDate != null ? "deactivationDate=" + deactivationDate + ", " : "") +
                (comments != null ? "comments=" + comments + ", " : "") +
                (additionalInformation != null ? "additionalInformation=" + additionalInformation + ", " : "") +
                (cardNumber != null ? "cardNumber=" + cardNumber + ", " : "") +
                (childId != null ? "childId=" + childId + ", " : "") +
                (childName != null ? "childName=" + childName + ", " : "") +
                (promoterItineraryId != null ? "promoterItineraryId=" + promoterItineraryId + ", " : "") +
                (promoterItineraryName != null ? "promoterItineraryName=" + promoterItineraryName + ", " : "") +
                (organizationId != null ? "organizationId=" + organizationId + ", " : "") +
                (promoterServiceId != null ? "promoterServiceId=" + promoterServiceId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (distinct != null ? "distinct=" + distinct + ", " : "") +
                "}";
    }
}
