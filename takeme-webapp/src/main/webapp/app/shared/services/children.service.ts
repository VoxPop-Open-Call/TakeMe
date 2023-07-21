import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {IChild} from "../model/child.model";
import {IChildItinerarySubscription} from "../../entities/child-itinerary-subscription/child-itinerary-subscription.model";
import {ITutor} from "../model/tutor.model";
import {IPhoto} from "../model/photo.model";
import {IService} from "../model/service.model";

@Injectable()
export class ChildrenService {
    constructor(
        private http: HttpClient
    ) {
    }

    public find(tutorId, childId): Observable<HttpResponse<IChild>> {
        return this.http.get<IChild>(`api/tutors/${tutorId}/children/${childId}`, {observe: 'response'});
    }

    public update(tutorId, child): Observable<HttpResponse<IChild>> {
        return this.http.patch<IChild>(`api/tutors/${tutorId}/children/${child.id}`, this.convertDateFromClient(child), {observe: 'response'});
    }

    public getChildren(tutorId, pagination?): Observable<HttpResponse<IChild[]>> {
        let params: HttpParams = new HttpParams();
        params = this.createPagination(params, pagination);
        return this.http.get<IChild[]>(`api/tutors/${tutorId}/children`, {params, observe: 'response'});
    }

    public getChildPhoto(childId, photoId): Observable<HttpResponse<IPhoto>> {
        return this.http.get<IPhoto>(`api/children/${childId}/photo/${photoId}`, {observe: 'response'});
    }

    public getChildItinerarySubscriptions(request, pagination?): Observable<HttpResponse<IChildItinerarySubscription[]>> {
        let params: HttpParams = new HttpParams();
        if (request.operatorId) {
            params = params.set('organizationId.equals', request.operatorId);
        }
        if (request.childId) {
            params = params.set('childId.equals', request.childId);
        }
        if (request.childName) {
            params = params.set('childName.contains', request.childName);
        }
        if (request.statusType) {
            params = params.set('statusType.equals', request.statusType);
        }
        if (request.promoterServiceId) {
            params = params.set('promoterServiceId.equals', request.promoterServiceId);
        }
        if (request.promoterItineraryId) {
            params = params.set('promoterItineraryId.equals', request.promoterItineraryId);
        }
        if (request.promoterItineraryName) {
            params = params.set('promoterItineraryName.contains', request.promoterItineraryName);
        }
        params = this.createPagination(params, pagination);
        return this.http.get<IChildItinerarySubscription[]>(`api/child-itinerary-subscriptions`, {params, observe: 'response'});
    }

    public createChildItinerarySubscription(request): Observable<HttpResponse<IChild>> {
        return this.http.post<IChild>('api/child-itinerary-subscriptions', request, {observe: 'response'});
    }

    public createChildren(tutorId, child): Observable<HttpResponse<IChild>> {
        return this.http.post<IChild>(`api/tutors/${tutorId}/children`, this.convertDateFromClient(child), {observe: 'response'});
    }

    public patchChildItinerarySubscription(subscriptionId, subscription, request): Observable<HttpResponse<IChildItinerarySubscription>> {
        return this.http.patch<IChildItinerarySubscription>(`api/child-itinerary-subscriptions/${subscriptionId}`, Object.assign({}, subscription, request), {observe: 'response'});
    }

    public getChildItinerarySubscription(subscriptionId): Observable<HttpResponse<IChildItinerarySubscription>> {
        return this.http.get<IChildItinerarySubscription>(`api/child-itinerary-subscriptions/${subscriptionId}`, {observe: 'response'});
    }

    public getChildTutors(childId): Observable<HttpResponse<ITutor[]>> {
        return this.http.get<ITutor[]>(`api/children/${childId}/tutors`, {observe: 'response'});
    }

    private convertDateFromClient(data) {
        return Object.assign({}, data, {
            dateOfBirth: data.dateOfBirth != null ? data.dateOfBirth.format("YYYY-MM-DD") : null
        });
    }

    public getServicesBySubscriptionId(subscriptionId, pagination?): Observable<HttpResponse<IService[]>> {
        let params: HttpParams = new HttpParams();
        if (pagination && pagination.page) params = params.set('page', String(pagination.page));
        if (pagination && pagination.itemsPerPage) params = params.set('size', String(pagination.itemsPerPage));
        return this.http.get<IService[]>(`api/child-itinerary-subscriptions/${subscriptionId}/services`, {params, observe: 'response'});
    }

    private createPagination(params, pagination?): HttpParams {
        if (pagination && pagination.page) {
            params = params.set('page', String(pagination.page));
        }
        if (pagination && pagination.itemsPerPage) {
            params = params.set('size', String(pagination.itemsPerPage));
        }
        if (pagination && pagination.sortProperty && pagination.sortDirection) {
            params = params.set('sort', `${pagination.sortProperty},${pagination.sortDirection}`);
        }
        return params;
    }
}
