package pt.famility.backoffice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import pt.famility.backoffice.domain.enumeration.TransportType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A PromoterService.
 */
@Entity
@Table(name = "promoter_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PromoterService extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Column(name = "needs_eta", nullable = false)
    private Boolean needsETA;

    @NotNull
    @Column(name = "enrollment_url", nullable = false)
    private String enrollmentURL;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transport_type", nullable = false)
    private TransportType transportType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PromoterService id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PromoterService name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public PromoterService logo(byte[] logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public PromoterService logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public PromoterService startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public PromoterService endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getNeedsETA() {
        return this.needsETA;
    }

    public PromoterService needsETA(Boolean needsETA) {
        this.setNeedsETA(needsETA);
        return this;
    }

    public void setNeedsETA(Boolean needsETA) {
        this.needsETA = needsETA;
    }

    public String getEnrollmentURL() {
        return this.enrollmentURL;
    }

    public PromoterService enrollmentURL(String enrollmentURL) {
        this.setEnrollmentURL(enrollmentURL);
        return this;
    }

    public void setEnrollmentURL(String enrollmentURL) {
        this.enrollmentURL = enrollmentURL;
    }

    public TransportType getTransportType() {
        return this.transportType;
    }

    public PromoterService transportType(TransportType transportType) {
        this.setTransportType(transportType);
        return this;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromoterService)) {
            return false;
        }
        return id != null && id.equals(((PromoterService) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromoterService{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", needsETA='" + getNeedsETA() + "'" +
            ", enrollmentURL='" + getEnrollmentURL() + "'" +
            ", transportType='" + getTransportType() + "'" +
            "}";
    }
}
