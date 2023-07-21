import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {IServiceStopPointDaysOfWeek} from 'app/shared/model/service-stop-point-days-of-week.model';

@Injectable()
export class ServiceStopPointDaysOfWeekService {
    constructor(
        private http: HttpClient
    ) {
    }

    public getAll(): Observable<HttpResponse<IServiceStopPointDaysOfWeek[]>> {
        return this.http.get<IServiceStopPointDaysOfWeek[]>('api/service-stop-point-days-of-weeks', {observe: 'response'});
    }
}
