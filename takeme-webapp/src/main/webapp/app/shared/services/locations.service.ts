import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ILocation} from 'app/shared/model/location.model';

@Injectable()
export class LocationsService {
    constructor(
        private http: HttpClient
    ) {
    }

    public get(locationId): Observable<HttpResponse<ILocation>> {
        return this.http.get<ILocation>(`api/locations/${locationId}`, {observe: 'response'});
    }

    public update(request): Observable<HttpResponse<ILocation>> {
        return this.http.put<ILocation>(`api/locations`, request, {observe: 'response'});
    }
}
