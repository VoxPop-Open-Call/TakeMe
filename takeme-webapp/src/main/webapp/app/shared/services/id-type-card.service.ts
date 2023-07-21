import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IIdentificationCardType } from 'app/shared/model/identification-card-type.model';

@Injectable()
export class IdTypeCardService {
    constructor(private http: HttpClient) {}

    getAllIdTypeCards(): Observable<HttpResponse<IIdentificationCardType[]>> {
        return this.http.get<IIdentificationCardType[]>('api/identification-card-types', { observe: 'response' });
    }
}
