import { Injectable } from '@angular/core';
import { Itinerary } from '../models/itinerary';

@Injectable({
    providedIn: 'root'
})
export class CurrentItineraryProvider {

    currentItinerary: Itinerary;

    constructor() {
    }

    setCurrentItinerary(currentItinerary: Itinerary) {
        this.currentItinerary = currentItinerary;
    }

    hasCurrentItinerary() {
        return this.currentItinerary != null;
    }

    getCurrentItineraryAndResetProvider(): Itinerary {
        const currentItinerary = this.currentItinerary;
        this.currentItinerary = null;
        return currentItinerary;
    }

}
