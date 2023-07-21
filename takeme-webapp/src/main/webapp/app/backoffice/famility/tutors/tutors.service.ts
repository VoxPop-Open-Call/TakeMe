import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import moment from 'moment';
import {map} from 'rxjs/operators';
import {ITutor} from 'app/shared/model/tutor.model';
import {IChild} from 'app/shared/model/child.model';
import {IChildSubscriptionChild} from 'app/shared/model/child-subscription-child.model';

@Injectable()
export class TutorsService {
    constructor(
        private http: HttpClient
    ) {
    }

    public getTutors(request, pagination): Observable<HttpResponse<ITutor[]>> {
        let params: HttpParams = new HttpParams();
        params = params.set('name', request.tutorName);
        params = params.set('status', request.tutorStatus);
        params = params.set('page', String(pagination.page));
        params = params.set('size', String(pagination.itemsPerPage));
        params = params.set('sort', `${pagination.sortProperty},${pagination.sortDirection}`);
        return this.http
            .get<ITutor[]>(`api/tutors`, {params, observe: 'response'})
            .pipe(map((res: HttpResponse<ITutor[]>) => this.convertDateArrayFromServer(res)));
    }

    getTutor(idTutor: string): Observable<HttpResponse<ITutor>> {
        return this.http.get<ITutor>(`/api/tutors/${idTutor}`, {observe: 'response'});
    }

    getChildrenByTutor(idTutor: string): Observable<HttpResponse<IChild[]>> {
        return this.http.get<IChild[]>(`/api/tutors/${idTutor}/children`, {observe: 'response'});
    }

    getOrganizationOfChild(idChild: number): Observable<HttpResponse<IChildSubscriptionChild[]>> {
        return this.http.get<IChildSubscriptionChild[]>(`/api/children/${idChild}/child-subscriptions?page=0&size=100`, {
            observe: 'response'
        });
    }

    public setTutorStatus(id, status): Observable<HttpResponse<Object>> {
        return this.http.patch(`api/tutors/${id}`, {status: status}, {observe: 'response'});
    }

    private convertDateArrayFromServer(res: HttpResponse<ITutor[]>): HttpResponse<ITutor[]> {
        if (res.body) {
            res.body.forEach((tutor: ITutor) => {
                tutor.createdDate = tutor.createdDate != null ? moment(tutor.createdDate) : null;
                tutor.lastModifiedDate = tutor.lastModifiedDate != null ? moment(tutor.lastModifiedDate) : null;
            });
        }
        return res;
    }
}
