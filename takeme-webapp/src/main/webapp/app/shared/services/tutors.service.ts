import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ITutor, StatusType } from 'app/shared/model/tutor.model';
import { IItineraryStopPointChildView } from "../model/itinerary-stop-point-shild-view.model";
import { IChild } from "../model/child.model";

@Injectable()
export class TutorsService {
    constructor(private http: HttpClient) {}

    getTutors(
        nameFilter: string,
        idOrganization: string,
        isFamility: boolean,
        status: StatusType,
        page: number,
        pageSize: number
    ): Observable<HttpResponse<ITutor[]>> {
        return this.http.get<ITutor[]>(
            `api/tutors?name=${nameFilter}&idOrganization=${idOrganization}&isFamility=${isFamility}&status=${status}&page=${page}&size=${pageSize}`,
            { observe: 'response' }
        );
    }

    getTutor(id): Observable<HttpResponse<ITutor>> {
        return this.http.get<ITutor>(`api/tutors/${id}`, { observe: 'response' });
    }

    getChildren(id): Observable<HttpResponse<IChild[]>> {
        return this.http.get<IChild[]>(`api/tutors/${id}/children`, { observe: 'response' });
    }

    patchTutor(id, phoneNumber): Observable<HttpResponse<any>> {
        const data = { phoneNumber };
        return this.http.patch(`api/tutors/${id}`, data, { observe: 'response' });
    }

    getByChildId(params: HttpParams): Observable<HttpResponse<IItineraryStopPointChildView[]>> {
        return this.http.get<IItineraryStopPointChildView[]>(`api/tutors/itineraries/by-child`, {params, observe: 'response'});
    }
}
