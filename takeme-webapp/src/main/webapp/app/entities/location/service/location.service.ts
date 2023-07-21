import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILocation } from '../location.model';

export type PartialUpdateLocation = Partial<ILocation> & Pick<ILocation, 'id'>;

export type EntityResponseType = HttpResponse<ILocation>;
export type EntityArrayResponseType = HttpResponse<ILocation[]>;

@Injectable({ providedIn: 'root' })
export class LocationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/locations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

    create(location: ILocation | (Omit<ILocation, "id"> & { id: null })): Observable<EntityResponseType> {
    return this.http.post<ILocation>(this.resourceUrl, location, { observe: 'response' });
  }

  update(location: ILocation): Observable<EntityResponseType> {
    return this.http.put<ILocation>(`${this.resourceUrl}/${this.getLocationIdentifier(location)}`, location, { observe: 'response' });
  }

  partialUpdate(location: PartialUpdateLocation): Observable<EntityResponseType> {
    return this.http.patch<ILocation>(`${this.resourceUrl}/${this.getLocationIdentifier(location)}`, location, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILocation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILocation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLocationIdentifier(location: Pick<ILocation, 'id'>): number {
    return location.id;
  }

  compareLocation(o1: Pick<ILocation, 'id'> | null, o2: Pick<ILocation, 'id'> | null): boolean {
    return o1 && o2 ? this.getLocationIdentifier(o1) === this.getLocationIdentifier(o2) : o1 === o2;
  }

  addLocationToCollectionIfMissing<Type extends Pick<ILocation, 'id'>>(
    locationCollection: Type[],
    ...locationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const locations: Type[] = locationsToCheck.filter(isPresent);
    if (locations.length > 0) {
      const locationCollectionIdentifiers = locationCollection.map(locationItem => this.getLocationIdentifier(locationItem)!);
      const locationsToAdd = locations.filter(locationItem => {
        const locationIdentifier = this.getLocationIdentifier(locationItem);
        if (locationCollectionIdentifiers.includes(locationIdentifier)) {
          return false;
        }
        locationCollectionIdentifiers.push(locationIdentifier);
        return true;
      });
      return [...locationsToAdd, ...locationCollection];
    }
    return locationCollection;
  }

    public generateGoogleMapsURL(coordinates: { longitude: string; latitude: string }[], travelMode) {
        let origin = '';
        let destination = '';
        let waypoints = '';

        if (coordinates.length > 0) {
            origin = `${coordinates[0].latitude},${coordinates[0].longitude}`;
            destination = `${coordinates[coordinates.length - 1].latitude},${coordinates[coordinates.length - 1].longitude}`;

            if (coordinates.length > 2) {
                waypoints = coordinates
                    .slice(1, coordinates.length - 1)
                    .map(waypoint => `${waypoint.latitude},${waypoint.longitude}`)
                    .join('|');
            }
        }

        let encodedOrigin = encodeURIComponent(origin);
        let encodedDestination = encodeURIComponent(destination);
        let encodedWaypoints = encodeURIComponent(waypoints);
        let encodedTravelMode = encodeURIComponent(travelMode);
        return `https://www.google.com/maps/dir/?api=1&origin=${encodedOrigin}&destination=${encodedDestination}&waypoints=${encodedWaypoints}&travelmode=${encodedTravelMode}`;
    }
}
