package pt.famility.backoffice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import pt.famility.backoffice.domain.enumeration.ItineraryStatusType;
import pt.famility.backoffice.domain.enumeration.LocationType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A ItineraryStopPointChildView.
 */
@Entity
@Table(name = "itinerary_stop_point_child_v")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@IdClass(ItineraryStopPointChildViewId.class)
public class ItineraryStopPointChildView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column(name = "child_id", nullable = false)
    private Long childId;

    @Id
    @NotNull
    @Column(name = "itinerary_id", nullable = false)
    private Long itineraryId;

    @NotNull
    @Column(name = "child_name", nullable = false)
    private String childName;

    @NotNull
    @Column(name = "itinerary_name", nullable = false)
    private String itineraryName;

    @NotNull
    @Column(name = "collection", nullable = false)
    private String collection;

    @NotNull
    @Column(name = "deliver", nullable = false)
    private String deliver;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "itinerary_status_type", nullable = false)
    private ItineraryStatusType itineraryStatusType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public Long getItineraryId() { return itineraryId; }

    public void setItineraryId(Long itineraryId) { this.itineraryId = itineraryId; }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getItineraryName() {
        return itineraryName;
    }

    public void setItineraryName(String itineraryName) {
        this.itineraryName = itineraryName;
    }

    public LocalDateTime getCollectionScheduledTime() {
        return LocalDateTime.parse(collection.split(";")[0].replace(" ", "T"));
    }

    public void setCollectionScheduledTime(LocalDateTime collectionScheduledTime) {
        String restOfCollection = collection.split(";")[1];
        this.collection = collectionScheduledTime.toString().replace("T", " ") + ";" + restOfCollection.trim();
    }

    public Location getCollectionLocation() {
        String[] locationString = collection.split(";")[1]
                                                .replace("(", "")
                                                .replace(")", "")
                                                .split(",");
        Location result = new Location();
        result.setId(Long.valueOf(locationString[0]));
        result.setDesignation(locationString[1]);
        result.setStreet(locationString[2]);
        result.setPortNumber(locationString[3]);
        result.setFloor(locationString[4]);
        result.setPostalCode(locationString[5]);
        result.setCity(locationString[6]);
        result.setCountry(locationString[7]);
        result.setType(LocationType.valueOf(locationString[8]));
        result.setLongitude(locationString[9]);
        result.setLatitude(locationString[10]);
        result.setPlusCode(locationString.length == 12 ? locationString[11] : null);
        return result;
    }

    public void setCollectionLocation(Location location) {
        String restOfCollection = collection.split(";")[0];
        this.collection = restOfCollection.trim() + ";(" +
            location.getId() + ",\"" +
            location.getDesignation() + "\",\"" +
            location.getStreet() + "\"," +
            location.getPortNumber() + "," +
            location.getFloor() + ",\"" +
            location.getPostalCode() + "\",\"" +
            location.getCity() + "\",\"" +
            location.getCountry() + "\"," +
            location.getType() + "," +
            location.getLongitude() + "," +
            location.getLatitude() + "," +
            location.getPlusCode() + ")";
    }

    public LocalDateTime getDeliverScheduledTime() {
        return LocalDateTime.parse(deliver.split(";")[0].replace(" ", "T"));
    }

    public void setDeliverScheduledTime(LocalDateTime collectionScheduledTime) {
        String restOfCollection = deliver.split(";")[1];
        this.deliver = collectionScheduledTime.toString().replace("T", " ") + ";" + restOfCollection.trim();
    }

    public Location getDeliverLocation() {
        String[] locationString = deliver.split(";")[1]
                                                .replace("(", "")
                                                .replace(")", "")
                                                .split(",");
        Location result = new Location();
        result.setId(Long.valueOf(locationString[0]));
        result.setDesignation(locationString[1]);
        result.setStreet(locationString[2]);
        result.setPortNumber(locationString[3]);
        result.setFloor(locationString[4]);
        result.setPostalCode(locationString[5]);
        result.setCity(locationString[6]);
        result.setCountry(locationString[7]);
        result.setType(LocationType.valueOf(locationString[8]));
        result.setLongitude(locationString[9]);
        result.setLatitude(locationString[10]);
        result.setPlusCode(locationString.length == 12 ? locationString[11] : null);
        return result;
    }

    public void setDeliverLocation(Location location) {
        String restOfCollection = deliver.split(";")[0];
        this.deliver = restOfCollection.trim() + ";(" +
            location.getId() + ",\"" +
            location.getDesignation() + "\",\"" +
            location.getStreet() + "\"," +
            location.getPortNumber() + "," +
            location.getFloor() + ",\"" +
            location.getPostalCode() + "\",\"" +
            location.getCity() + "\",\"" +
            location.getCountry() + "\"," +
            location.getType() + "," +
            location.getLongitude() + "," +
            location.getLatitude() + "," +
            location.getPlusCode() + ")";
    }
    public ItineraryStatusType getItineraryStatusType() {
        return itineraryStatusType;
    }

    public void setItineraryStatusType(ItineraryStatusType itineraryStatusType) {
        this.itineraryStatusType = itineraryStatusType;
    }

    @Override
    public String toString() {
        return "ItineraryStopPointChildView{" +
            "childId=" + getChildId() +
            "itineraryId=" + getItineraryId() +
            "childName=" + getChildName() +
            "itineraryName=" + getItineraryName() +
            "itineraryStatusType=" + getItineraryStatusType() +
            "}";
    }
}
