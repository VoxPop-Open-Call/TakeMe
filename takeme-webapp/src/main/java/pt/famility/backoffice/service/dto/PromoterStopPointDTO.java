package pt.famility.backoffice.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link pt.famility.backoffice.domain.PromoterStopPoint} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PromoterStopPointDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Instant scheduledTime;

    private PromoterItineraryDTO promoterItinerary;

    private LocationDTO location;

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

    public Instant getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Instant scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public PromoterItineraryDTO getPromoterItinerary() {
        return promoterItinerary;
    }

    public void setPromoterItinerary(PromoterItineraryDTO promoterItinerary) {
        this.promoterItinerary = promoterItinerary;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromoterStopPointDTO)) {
            return false;
        }

        PromoterStopPointDTO promoterStopPointDTO = (PromoterStopPointDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, promoterStopPointDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromoterStopPointDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", scheduledTime='" + getScheduledTime() + "'" +
            ", promoterItinerary=" + getPromoterItinerary() +
            ", location=" + getLocation() +
            "}";
    }
}
