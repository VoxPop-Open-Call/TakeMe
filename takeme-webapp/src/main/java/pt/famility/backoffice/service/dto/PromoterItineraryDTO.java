package pt.famility.backoffice.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link pt.famility.backoffice.domain.PromoterItinerary} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PromoterItineraryDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    private PromoterServiceDTO service;

    private OrganizationDTO organization;

    private List<PromoterStopPointDTO> promoterItineraryStopPoints;

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

    public PromoterServiceDTO getService() {
        return service;
    }

    public void setService(PromoterServiceDTO service) {
        this.service = service;
    }

    public OrganizationDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDTO organization) {
        this.organization = organization;
    }

    public List<PromoterStopPointDTO> getPromoterItineraryStopPoints() { return promoterItineraryStopPoints; }

    public void setPromoterItineraryStopPoints(List<PromoterStopPointDTO> promoterItineraryStopPoints) { this.promoterItineraryStopPoints = promoterItineraryStopPoints; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromoterItineraryDTO)) {
            return false;
        }

        PromoterItineraryDTO promoterItineraryDTO = (PromoterItineraryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, promoterItineraryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromoterItineraryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", service=" + getService() +
            ", organization=" + getOrganization() +
            "}";
    }
}
