package pt.famility.backoffice.domain;

import java.io.Serializable;

/**
 * A ItineraryStopPointChildViewId.
 */
public class ItineraryStopPointChildViewId implements Serializable {
    private Long childId;

    private Long itineraryId;

    public ItineraryStopPointChildViewId(Long childId, Long itineraryId){
        this.childId = childId;
        this.itineraryId = itineraryId;
    }

    public ItineraryStopPointChildViewId() {}
}
