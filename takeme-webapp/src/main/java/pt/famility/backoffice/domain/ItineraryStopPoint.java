package pt.famility.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pt.famility.backoffice.domain.enumeration.ItineraryStatusType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "itinerary_stop_point")
public class ItineraryStopPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    private Instant estimatedTime;

    @Column(name = "jhi_order")
    private Integer order;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "itinerary_status_type", nullable = false)
    private ItineraryStatusType itineraryStatusType;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "id_stop_point")
    @JsonIgnoreProperties("")
    private StopPoint stopPoint;

    @ManyToOne
    @JoinColumn(name = "id_itinerary")
    @JsonIgnoreProperties("")
    private Itinerary itinerary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StopPoint getStopPoint() {
        return stopPoint;
    }

    public void setStopPoint(StopPoint stopPoint) {
        this.stopPoint = stopPoint;
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public Instant getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Instant estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public ItineraryStatusType getItineraryStatusType() {
        return itineraryStatusType;
    }

    public void setItineraryStatusType(ItineraryStatusType itineraryStatusType) {
        this.itineraryStatusType = itineraryStatusType;
    }

    public ItineraryStopPoint id(Long id) {
        this.id = id;
        return this;
    }

    public ItineraryStopPoint estimatedTime(Instant estimatedTime) {
        this.estimatedTime = estimatedTime;
        return this;
    }

    public ItineraryStopPoint order(Integer order) {
        this.order = order;
        return this;
    }

    public ItineraryStopPoint stopPoint(StopPoint stopPoint) {
        this.stopPoint = stopPoint;
        return this;
    }

    public ItineraryStopPoint itinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItineraryStopPoint that = (ItineraryStopPoint) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(estimatedTime, that.estimatedTime) &&
            Objects.equals(order, that.order) &&
            Objects.equals(stopPoint, that.stopPoint) &&
            Objects.equals(itinerary, that.itinerary);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
