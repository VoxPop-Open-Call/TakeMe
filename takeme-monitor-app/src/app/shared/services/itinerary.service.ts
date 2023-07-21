import { Injectable } from '@angular/core';
import { Itinerary } from '../models/itinerary';
import { BACKEND_BASE_URL } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { ChildStopEventDTO } from '../models/child-stop-event-dto';
import { ItineraryStatusTypePatchDTO } from '../models/itinerary-status-type-patch-DTO';

@Injectable({
    providedIn: 'root'
})
export class ItineraryService {

    private ITINERARIES_URL = BACKEND_BASE_URL + '/api/itineraries';

    constructor(private http: HttpClient) {
    }

    getItineraryDetails(itineraryId: string) {
        return this.http.get(this.ITINERARIES_URL + '/' + itineraryId, {observe: 'response'});
    }

    updateItinerary(itinerary: Itinerary) {
        return this.http.put(this.ITINERARIES_URL, itinerary, {observe: 'response'});
    }

    startItinerary(itineraryId: string, itineraryStatusTypePatchDTO: ItineraryStatusTypePatchDTO) {
        return this.http.patch(this.ITINERARIES_URL + '/' + itineraryId + '/status', itineraryStatusTypePatchDTO, {observe: 'response'});
    }

    finishStopPoint(itineraryId: string, stopManagementArray: ChildStopEventDTO[]) {
        return this.http.post<Itinerary>(this.ITINERARIES_URL + '/' + itineraryId + '/finish-stop-point', stopManagementArray, {observe: 'response'});
    }

}
