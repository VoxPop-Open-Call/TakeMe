import { Injectable } from '@angular/core';
import { BACKEND_BASE_URL } from '../../../environments/environment';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Contact } from '../models/contact';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LocationService {

    private LOCATION_URL = BACKEND_BASE_URL + '/api/location';

    constructor(private http: HttpClient) {
    }

    getOrganizationPublicContactsByLocationId(locationId: string): Observable<HttpResponse<Contact[]>> {
        return this.http.get<Contact[]>(this.LOCATION_URL + '/' + locationId + '/contacts', { observe: 'response' });
    }

}
