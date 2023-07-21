import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import moment from 'moment';

import { Itinerary, ItineraryStatusType } from 'app/shared/model/itinerary.model';
import { ItineraryStopPoint } from 'app/shared/model/itinerary-stop-point.model';
import { Child } from 'app/shared/model/child.model';

@Injectable()
export class ItineraryService {
    constructor(private http: HttpClient) {}

    create(itinerary): Observable<HttpResponse<Itinerary>> {
        return this.http.post<Itinerary>('api/itineraries', this.convertDateFromClient(itinerary), { observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`api/itineraries/${id}`, { observe: 'response' });
    }

    update(itinerary): Observable<HttpResponse<Itinerary>> {
        return this.http.put<Itinerary>('api/itineraries', this.convertDateFromClient(itinerary), { observe: 'response' });
    }

    getByIdOrganization(
        idOrganization: number,
        name: string,
        statusTypes: ItineraryStatusType[],
        page: number,
        size: number,
        sortProperty: string,
        sortDirection: string
    ): Observable<HttpResponse<Itinerary[]>> {
        let params: HttpParams = new HttpParams();
        params = params.set('page', String(page));
        params = params.set('size', String(size));
        params = params.set('sort', sortProperty + ',' + sortDirection);
        return this.http
            .get<Itinerary[]>(`api/itineraries?id=${idOrganization}&name=${name}&statusTypes=${statusTypes.toString()}`, {
                params,
                observe: 'response'
            })
            .pipe(map((res: HttpResponse<Itinerary[]>) => this.convertArrayDateFromServer(res)));
    }

    patchStatus(id: number, statusType: ItineraryStatusType): Observable<HttpResponse<any>> {
        const data = { statusType };
        return this.http.patch<any>(`api/itineraries/${id}/status`, data, { observe: 'response' });
    }

    get(id: number): Observable<HttpResponse<Itinerary>> {
        return this.http
            .get<Itinerary>(`api/itineraries/${id}`, { observe: 'response' })
            .pipe(map((res: HttpResponse<Itinerary>) => this.convertDateDetailFromServer(res)));
    }

    protected convertDateFromClient(itinerary) {
        const itineraryStopPointList = [];

        itinerary.itineraryStopPointList.forEach(itineraryStopPoint => {
            itineraryStopPoint.stopPoint.scheduledTime =
                itineraryStopPoint.stopPoint.scheduledTime != null && itineraryStopPoint.stopPoint.scheduledTime.isValid()
                    ? itineraryStopPoint.stopPoint.scheduledTime.toJSON()
                    : null;

            itineraryStopPointList.push(itineraryStopPoint);
        });

        const copy = Object.assign({}, itinerary, {
            scheduledTime: itinerary.scheduledTime != null && itinerary.scheduledTime.isValid() ? itinerary.scheduledTime.toJSON() : null,
            itineraryStopPointList
        });

        return copy;
    }

    protected convertDateDetailFromServer(res: HttpResponse<Itinerary>): HttpResponse<Itinerary> {
        if (res.body) {
            res.body.scheduledTime = res.body.scheduledTime != null ? moment(res.body.scheduledTime) : null;
            res.body.effectiveStartTime = res.body.effectiveStartTime != null ? moment(res.body.effectiveStartTime) : null;
            res.body.effectiveEndTime = res.body.effectiveEndTime != null ? moment(res.body.effectiveEndTime) : null;

            res.body.itineraryStopPointList.forEach((itineraryStopPoint: ItineraryStopPoint) => {
                itineraryStopPoint.stopPoint.scheduledTime =
                    itineraryStopPoint.stopPoint.scheduledTime != null ? moment(itineraryStopPoint.stopPoint.scheduledTime) : null;
                itineraryStopPoint.stopPoint.estimatedArriveTime =
                    itineraryStopPoint.stopPoint.estimatedArriveTime != null
                        ? moment(itineraryStopPoint.stopPoint.estimatedArriveTime)
                        : null;
                itineraryStopPoint.stopPoint.effectiveArriveTime =
                    itineraryStopPoint.stopPoint.effectiveArriveTime != null
                        ? moment(itineraryStopPoint.stopPoint.effectiveArriveTime)
                        : null;

                if (itineraryStopPoint.stopPoint) {
                    itineraryStopPoint.stopPoint.childList.forEach((child: Child) => {
                        child.dateOfBirth = child.dateOfBirth != null ? moment(child.dateOfBirth) : null;
                    });
                }
            });
        }

        return res;
    }

    protected convertArrayDateFromServer(res: HttpResponse<Itinerary[]>): HttpResponse<Itinerary[]> {
        if (res.body) {
            res.body.forEach((itinerary: Itinerary) => {
                itinerary.scheduledTime = itinerary.scheduledTime != null ? moment(itinerary.scheduledTime) : null;
                itinerary.effectiveStartTime = itinerary.effectiveStartTime != null ? moment(itinerary.effectiveStartTime) : null;
                itinerary.effectiveEndTime = itinerary.effectiveEndTime != null ? moment(itinerary.effectiveEndTime) : null;
            });
        }
        return res;
    }
}
