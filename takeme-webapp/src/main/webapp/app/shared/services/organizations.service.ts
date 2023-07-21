import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import {ITutor} from "app/shared/model/tutor.model";
import {IChild} from "app/shared/model/child.model";
import {IOrganization, OrganizationType} from "app/shared/model/organization.model";
import {IDriver} from "app/shared/model/driver.model";
import {IVehicle} from "app/shared/model/vehicle.model";
import {ILocation} from "app/shared/model/location.model";

@Injectable({providedIn: 'root'})
export class OrganizationsService {
    constructor(
        private http: HttpClient
    ) {
    }

    public getOperators(request, pagination): Observable<HttpResponse<IOrganization[]>> {
        let params: HttpParams = new HttpParams();
        params = params.set('name', request.operatorName);
        params = params.set('type', request.operatorType);
        params = params.set('status', request.operatorStatus);
        params = params.set('page', String(pagination.page));
        params = params.set('size', String(pagination.itemsPerPage));
        params = params.set('sort', `${pagination.sortProperty},${pagination.sortDirection}`);
        return this.http.get<IOrganization[]>(`api/organizations`, {params, observe: 'response'});
    }

    protected static convertDateFromClient(data) {
        return Object.assign({}, data, {
            dateOfBirthday: data.dateOfBirthday != null && data.dateOfBirthday.isValid() ? data.dateOfBirthday.toJSON() : null
        });
    }

    createTutorByOrganization(
        idOrganization: string,
        email: string,
        firstName: string,
        lastName: string,
        nifNumber: string,
        nifCountry: string,
        identificationCardTypeName: string,
        identificationCardNumber: string,
        phoneNumber: string,
        photo: string
    ): Observable<HttpResponse<ITutor>> {
        const data = {
            email,
            firstName,
            lastName,
            nifCountry,
            nifNumber,
            identificationCardTypeName,
            identificationCardNumber,
            phoneNumber,
            photo
        };
        return this.http.post(`api/organizations/${idOrganization}/tutor`, data, {observe: 'response'});
    }

    createChildByOrganization(
        idOrganization: string,
        idTutor: string,
        name: string,
        nifNumber: string,
        nifCountry: string,
        dateOfBirth,
        photo: string
    ): Observable<HttpResponse<IChild>> {
        const data = {name, nifNumber, nifCountry, dateOfBirth, photo};
        return this.http.post(
            `api/organizations/${idOrganization}/tutor/${idTutor}/child`,
            OrganizationsService.convertDateFromClient(data),
            {
                observe: 'response'
            }
        );
    }

    getOrganizationsByType(
        name: string,
        type: OrganizationType,
        status: string,
        page: number,
        pageSize: number
    ): Observable<HttpResponse<IOrganization[]>> {
        if (status) {
            return this.http.get<IOrganization[]>(
                `api/organizations?name=${name}&type=` + type + `&status=${status}&page=${page}&size=${pageSize}`,
                {observe: 'response'}
            );
        } else {
            return this.http.get<IOrganization[]>(`api/organizations?name=${name}&type=` + type + `&page=${page}&size=${pageSize}`, {
                observe: 'response'
            });
        }
    }

    getOrganizationDetail(id: number): Observable<HttpResponse<IOrganization>> {
        return this.http.get<IOrganization>(`api/organizations/${id}`, {observe: 'response'});
    }

    updateOrganization(organization: IOrganization): Observable<HttpResponse<IOrganization>> {
        return this.http.put('api/organizations', organization, {observe: 'response'});
    }

    createOrganization(organization: IOrganization): Observable<HttpResponse<IOrganization>> {
        return this.http.post<IOrganization>('api/organizations', organization, {observe: 'response'});
    }

    public setOperatorStatus(id, status): Observable<HttpResponse<Object>> {
        return this.http.patch(`api/organizations/${id}`, {status: status}, {observe: 'response'});
    }

    getDrivers(id: number, page: number, pageSize: number, params?: HttpParams): Observable<HttpResponse<IDriver[]>> {
        return this.http.get<IDriver[]>(`api/organizations/${id}/drivers?page=${page}&size=${pageSize}`, {
            params,
            observe: 'response'
        });
    }

    getDriversWithoutUser(
        id: number,
        name: string,
        page: number,
        pageSize: number,
        params?: HttpParams
    ): Observable<HttpResponse<IDriver[]>> {
        return this.http.get<IDriver[]>(`api/organizations/${id}/drivers/without-user?name=${name}&page=${page}&size=${pageSize}`, {
            params,
            observe: 'response'
        });
    }

    getVehicles(id: number, page: number, pageSize: number, params?: HttpParams): Observable<HttpResponse<IVehicle[]>> {
        return this.http.get<IVehicle[]>(`api/organizations/${id}/vehicles?page=${page}&size=${pageSize}`, {
            params,
            observe: 'response'
        });
    }

    public createLocation(organizationId, request): Observable<HttpResponse<ILocation>> {
        return this.http.post<ILocation>(`api/organizations/${organizationId}/locations`, request, {observe: 'response'});
    }

    public getLocations(organizationId, request, pagination?): Observable<HttpResponse<ILocation[]>> {
        let params: HttpParams = new HttpParams();
        if (request.locationName) params = params.set('designation', request.locationName);
        if (pagination && pagination.page) params = params.set('page', String(pagination.page));
        if (pagination && pagination.itemsPerPage) params = params.set('size', String(pagination.itemsPerPage));
        return this.http.get<ILocation[]>(`api/organizations/${organizationId}/locations`, {params, observe: 'response'});
    }

    public deleteOrganizationServiceLocation(organizationId, locationId): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`api/organizations/${organizationId}/locations/${locationId}`, {observe: 'response'});
    }
}
