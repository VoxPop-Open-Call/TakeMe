import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { BACKEND_BASE_URL } from '../../../environments/environment';
import { Vehicle } from '../models/vehicle';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class OrganizationService {

    private ORGANIZATIONS_URL = BACKEND_BASE_URL + '/api/organizations';

    constructor(private http: HttpClient) {
    }

    getVehiclesByOrganizationId(organizationId: string): Observable<HttpResponse<Vehicle[]>>  {
        return this.http.get<Vehicle[]>(this.ORGANIZATIONS_URL + '/' + organizationId + '/vehicles', { observe: 'response' });
    }
}
