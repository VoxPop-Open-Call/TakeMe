import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { createRequestOption } from 'app/shared';
import { IOrganization } from 'app/shared/model/organization.model';
import { ILocation } from "../location/location.model";

type EntityResponseType = HttpResponse<IOrganization>;
type EntityArrayResponseType = HttpResponse<IOrganization[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationService {
    public resourceUrl = SERVER_API_URL + 'api/organizations';

    constructor(private http: HttpClient) {}

    create(organization: IOrganization): Observable<EntityResponseType> {
        return this.http.post<IOrganization>(this.resourceUrl, organization, { observe: 'response' });
    }

    update(organization: IOrganization): Observable<EntityResponseType> {
        return this.http.put<IOrganization>(this.resourceUrl, organization, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IOrganization>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findLocationsByOrganization(id: string): Observable<HttpResponse<ILocation[]>> {
        return this.http.get<ILocation[]>(`${this.resourceUrl}/${id}/locations`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrganization[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getPhoto(id: number, idPhoto: number): Observable<HttpResponse<any>> {
        return this.http.get(`${this.resourceUrl}/${id}/photo/${idPhoto}`, { observe: 'response' });
    }

    patchPhoto(id: number, photo64: string): Observable<HttpResponse<any>> {
        const data = {
            photo: photo64
        };

        return this.http.patch(`${this.resourceUrl}/${id}/photo`, data, { observe: 'response' });
    }
}
