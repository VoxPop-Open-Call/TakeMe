import { Injectable } from '@angular/core';
import { Itinerary } from 'src/app/shared/models/itinerary';
import { ItineraryStatusType } from 'src/app/shared/enums/itinerary-status-type.enum';

@Injectable({
    providedIn: 'root'
})
export class ItineraryProvider {

    private itinerary: Itinerary;
    private itineraries: Itinerary[];

    constructor() { }

    getItinerary() {
        return this.itinerary;
    }

    getItineraryId() {
        return this.getItinerary().id;
    }

    setItinerary(itinerary: Itinerary) {
        this.itinerary = itinerary;
    }

    setItineraries(itineraries: Itinerary[]) {
        this.itineraries = itineraries;
    }

    hasActiveItineraries() {
        return this.itineraries.filter(it => it.itineraryStatusType == ItineraryStatusType.IN_PROGRESS).length > 0;
    }
}
