import { Injectable } from '@angular/core';
import { Itinerary } from '../models/itinerary';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BACKEND_BASE_URL } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class DriverService {

    private DRIVERS_URL = BACKEND_BASE_URL + '/api/drivers';

    constructor(private http: HttpClient) {
    }

    getItinerariesByDriverID(driverId: string) {
        return this.http.get<Itinerary[]>(this.DRIVERS_URL + '/' + driverId + '/itineraries', { observe: 'response' });
    }

    getCurrentItinerary(driverId: string): Observable<HttpResponse<Itinerary>> {
        return this.http.get<Itinerary>(this.DRIVERS_URL + '/' + driverId + '/itinerary/current', { observe: 'response' });
    }
}
