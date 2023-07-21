package pt.famility.backoffice.service.dto;

import pt.famility.backoffice.domain.enumeration.TransportType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link pt.famility.backoffice.domain.PromoterService} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PromoterServiceDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private byte[] logo;

    private String logoContentType;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private Boolean needsETA;

    @NotNull
    private String enrollmentURL;

    @NotNull
    private TransportType transportType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getNeedsETA() {
        return needsETA;
    }

    public void setNeedsETA(Boolean needsETA) {
        this.needsETA = needsETA;
    }

    public String getEnrollmentURL() {
        return enrollmentURL;
    }

    public void setEnrollmentURL(String enrollmentURL) {
        this.enrollmentURL = enrollmentURL;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromoterServiceDTO)) {
            return false;
        }

        PromoterServiceDTO promoterServiceDTO = (PromoterServiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, promoterServiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromoterServiceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", logo='" + getLogo() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", needsETA='" + getNeedsETA() + "'" +
            ", enrollmentURL='" + getEnrollmentURL() + "'" +
            ", transportType='" + getTransportType() + "'" +
            "}";
    }
}
