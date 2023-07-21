package pt.famility.backoffice.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link pt.famility.backoffice.domain.PromoterStopPoint} entity. This class is used
 * in {@link pt.famility.backoffice.web.rest.PromoterStopPointResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /promoter-stop-points?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PromoterStopPointCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private InstantFilter scheduledTime;

    private LongFilter promoterItineraryId;

    private LongFilter locationId;

    private Boolean distinct;

    public PromoterStopPointCriteria() {}

    public PromoterStopPointCriteria(PromoterStopPointCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.scheduledTime = other.scheduledTime == null ? null : other.scheduledTime.copy();
        this.promoterItineraryId = other.promoterItineraryId == null ? null : other.promoterItineraryId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PromoterStopPointCriteria copy() {
        return new PromoterStopPointCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public InstantFilter getScheduledTime() {
        return scheduledTime;
    }

    public InstantFilter scheduledTime() {
        if (scheduledTime == null) {
            scheduledTime = new InstantFilter();
        }
        return scheduledTime;
    }

    public void setScheduledTime(InstantFilter scheduledTime) {
        this.scheduledTime = scheduledTime;
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

    public LongFilter getLocationId() {
        return locationId;
    }

    public LongFilter locationId() {
        if (locationId == null) {
            locationId = new LongFilter();
        }
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
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
        final PromoterStopPointCriteria that = (PromoterStopPointCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(scheduledTime, that.scheduledTime) &&
            Objects.equals(promoterItineraryId, that.promoterItineraryId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, scheduledTime, promoterItineraryId, locationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromoterStopPointCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (scheduledTime != null ? "scheduledTime=" + scheduledTime + ", " : "") +
            (promoterItineraryId != null ? "promoterItineraryId=" + promoterItineraryId + ", " : "") +
            (locationId != null ? "locationId=" + locationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
