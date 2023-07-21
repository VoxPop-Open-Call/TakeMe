import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import moment from 'moment';
import {IService} from 'app/shared/model/service.model';

@Injectable()
export class ServicesService {
    constructor(
        private http: HttpClient
    ) {
    }

    public createService(service): Observable<HttpResponse<IService>> {
        return this.http.post<IService>('api/services', this.convertDateFromClient(service), {observe: 'response'});
    }

    public updateService(service): Observable<HttpResponse<IService>> {
        return this.http.put<IService>(`api/services`, this.convertDateFromClient(service), {observe: 'response'});
    }

    public deleteService(serviceId): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`api/services/${serviceId}`, {observe: 'response'});
    }

    public getService(serviceId): Observable<HttpResponse<IService>> {
        return this.http
            .get<IService>(`api/services/${serviceId}`, {observe: 'response'})
            .pipe(map((res: HttpResponse<IService>) => this.convertDateFromService(res)));
    }

    private convertDateFromService(res: HttpResponse<IService>): HttpResponse<IService> {
        const serviceStopPoints = [];

        res.body.serviceStopPoints.forEach(servicePoint => {
            servicePoint.startFrequencyDate = servicePoint.startFrequencyDate != null ? moment(servicePoint.startFrequencyDate) : null;
            serviceStopPoints.push(servicePoint);
        });

        if (res.body) {
            res.body.startDate = res.body.startDate != null ? moment(res.body.startDate) : null;
            res.body.endDate = res.body.endDate != null ? moment(res.body.endDate) : null;
            res.body.serviceStopPoints = serviceStopPoints;
        }

        return res;
    }

    private convertDateFromClient(service) {
        const servicePoints = [];

        service.serviceStopPoints.forEach(servicePoint => {
            servicePoint.startFrequencyDate = servicePoint.startFrequencyDate != null && servicePoint.startFrequencyDate.isValid() ? this.setToMidnightUTC(servicePoint.startFrequencyDate.toDate()).toJSON() : null;
            servicePoints.push(servicePoint);
        });

        return Object.assign({}, service, {
            startDate: service.startDate != null && service.startDate.isValid() ? this.setToMidnightUTC(service.startDate.toDate()).toJSON() : null,
            endDate: service.endDate != null && service.endDate.isValid() ? this.setToMidnightUTC(service.endDate.toDate()).toJSON() : null,
            serviceStopPoints: servicePoints
        });
    }

    private setToMidnightUTC(date: Date): Date {
        return new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate(), 0, 0, 0, 0));
    }
}
