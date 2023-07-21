import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {IServiceStopPointFrequency} from 'app/shared/model/service-stop-point-frequency.model';

@Injectable()
export class ServiceStopPointFrequencyService {
    constructor(
        private http: HttpClient
    ) {
    }

    public getAll(): Observable<HttpResponse<IServiceStopPointFrequency[]>> {
        return this.http.get<IServiceStopPointFrequency[]>('api/service-stop-point-frequencies', {observe: 'response'});
    }
}
