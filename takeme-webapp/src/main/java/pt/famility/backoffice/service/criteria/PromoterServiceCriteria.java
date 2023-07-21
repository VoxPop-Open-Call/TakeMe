package pt.famility.backoffice.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link pt.famility.backoffice.domain.PromoterService} entity. This class is used
 * in {@link pt.famility.backoffice.web.rest.PromoterServiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /promoter-services?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PromoterServiceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private BooleanFilter needsETA;

    private StringFilter enrollmentURL;

    private Boolean distinct;

    public PromoterServiceCriteria() {}

    public PromoterServiceCriteria(PromoterServiceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.needsETA = other.needsETA == null ? null : other.needsETA.copy();
        this.enrollmentURL = other.enrollmentURL == null ? null : other.enrollmentURL.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PromoterServiceCriteria copy() {
        return new PromoterServiceCriteria(this);
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

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            startDate = new LocalDateFilter();
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            endDate = new LocalDateFilter();
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public BooleanFilter getNeedsETA() {
        return needsETA;
    }

    public BooleanFilter needsETA() {
        if (needsETA == null) {
            needsETA = new BooleanFilter();
        }
        return needsETA;
    }

    public void setNeedsETA(BooleanFilter needsETA) {
        this.needsETA = needsETA;
    }

    public StringFilter getEnrollmentURL() {
        return enrollmentURL;
    }

    public StringFilter enrollmentURL() {
        if (enrollmentURL == null) {
            enrollmentURL = new StringFilter();
        }
        return enrollmentURL;
    }

    public void setEnrollmentURL(StringFilter enrollmentURL) {
        this.enrollmentURL = enrollmentURL;
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
        final PromoterServiceCriteria that = (PromoterServiceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(needsETA, that.needsETA) &&
            Objects.equals(enrollmentURL, that.enrollmentURL) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startDate, endDate, needsETA, enrollmentURL, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromoterServiceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (needsETA != null ? "needsETA=" + needsETA + ", " : "") +
            (enrollmentURL != null ? "enrollmentURL=" + enrollmentURL + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
