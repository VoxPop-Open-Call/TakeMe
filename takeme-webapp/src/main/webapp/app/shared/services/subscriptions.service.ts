import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import moment from 'moment';

import { IChildSubscriptionList } from 'app/shared/model/child-subscription-list.model';
import { IChildSubscriptionDetail } from 'app/shared/model/child-subscription-detail.model';
import { map } from 'rxjs/operators';
import { ImportDTO } from 'app/shared/model/import-dto';

@Injectable({
    providedIn: 'root'
})
export class SubscriptionsService {
    constructor(private httpClient: HttpClient) {}

    getSubscriptions(
        idOrganization: string,
        status: string,
        childName: string,
        additionalInfo: string,
        familityOnly: boolean,
        page: number,
        pageSize: number,
        sortProperty: string,
        sortDirection: string,
        famility?: boolean
    ): Observable<HttpResponse<IChildSubscriptionList[]>> {
        let params: HttpParams = new HttpParams();
        params = params.set('page', String(page));
        params = params.set('size', String(pageSize));
        params = params.set('sort', sortProperty + ',' + sortDirection);
        params = params.set('status', status);
        params = params.set('childName', childName);
        params = params.set('additionalInformation', additionalInfo);
        if (familityOnly != null) {
            params = params.set('familityOnly', String(familityOnly));
        } else if (famility != null) {
            params = params.set('famility', String(famility));
        }

        return this.httpClient.get<IChildSubscriptionList[]>(`api/organizations/${idOrganization}/child-subscriptions`, {
            params,
            observe: 'response'
        });
    }

    getSubscriptionDetail(idSubscription: number): Observable<HttpResponse<IChildSubscriptionDetail>> {
        return this.httpClient
            .get<IChildSubscriptionDetail>(`/api/child-subscriptions/${idSubscription}`, { observe: 'response' })
            .pipe(map((res: HttpResponse<IChildSubscriptionDetail>) => this.convertDateFromServerDetail(res)));
    }

    updateSubscriptionTutor(idSubscription: string, idTutor: string) {
        const data = { idTutor };
        return this.httpClient.patch(`api/child-subscriptions/${idSubscription}`, data, { observe: 'response' });
    }

    changeSubscriptionStatus(idSubscription: string, status: string, comments: string, additionalInformation: string) {
        const data = {
            comments,
            additionalInformation,
            statusType: status
        };

        return this.httpClient.patch(`api/child-subscriptions/${idSubscription}`, data, { observe: 'response' });
    }

    updateAdditionalInformation(idSubscription: string, additionalInformation: string) {
        const data = {
            additionalInformation
        };
        return this.httpClient.patch(`api/child-subscriptions/${idSubscription}`, data, { observe: 'response' });
    }

    updateSubscriptionInformation(idSubscription: string, additionalInformation: string, cardNumber: string) {
        const data = {
            additionalInformation,
            cardNumber
        };
        return this.httpClient.patch(`api/child-subscriptions/${idSubscription}`, data, { observe: 'response' });
    }

    protected convertDateFromServerList(res: HttpResponse<IChildSubscriptionList[]>): HttpResponse<IChildSubscriptionList[]> {
        if (res.body) {
            res.body.forEach(childSubscriptionList => {
                childSubscriptionList.subscriptionDate =
                    childSubscriptionList.subscriptionDate != null ? moment(childSubscriptionList.subscriptionDate) : null;
                childSubscriptionList.activationDate =
                    childSubscriptionList.activationDate != null ? moment(childSubscriptionList.activationDate) : null;
                childSubscriptionList.deactivationDate =
                    childSubscriptionList.deactivationDate != null ? moment(childSubscriptionList.deactivationDate) : null;
            });
        }
        return res;
    }

    protected convertDateFromServerDetail(res: HttpResponse<IChildSubscriptionDetail>): HttpResponse<IChildSubscriptionDetail> {
        if (res.body) {
            res.body.child.dateOfBirth = res.body.child.dateOfBirth != null ? moment(res.body.child.dateOfBirth) : null;
            res.body.subscriptionDate = res.body.subscriptionDate != null ? moment(res.body.subscriptionDate) : null;
            res.body.activationDate = res.body.activationDate != null ? moment(res.body.activationDate) : null;
            res.body.deactivationDate = res.body.deactivationDate != null ? moment(res.body.deactivationDate) : null;
        }
        return res;
    }

    download(): Observable<HttpResponse<any>> {
        return this.httpClient.get('api/child-subscriptions/download', { observe: 'response', responseType: 'blob' });
    }

    upload(file: File): Observable<HttpResponse<ImportDTO>> {
        const formdata: FormData = new FormData();
        formdata.append('file', file);
        return this.httpClient.post<ImportDTO>('api/child-subscriptions/upload', formdata, { observe: 'response' });
    }
}
