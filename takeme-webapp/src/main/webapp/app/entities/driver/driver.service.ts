import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { createRequestOption } from 'app/shared';
import { IDriver } from 'app/shared/model/driver.model';

type EntityResponseType = HttpResponse<IDriver>;
type EntityArrayResponseType = HttpResponse<IDriver[]>;

@Injectable({ providedIn: 'root' })
export class DriverService {
    public resourceUrl = SERVER_API_URL + 'api/drivers';

    constructor(private http: HttpClient) {}

    create(driver: IDriver): Observable<EntityResponseType> {
        return this.http.post<IDriver>(this.resourceUrl, driver, { observe: 'response' });
    }

    update(driver: IDriver): Observable<EntityResponseType> {
        return this.http.put<IDriver>(this.resourceUrl, driver, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDriver>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDriver[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
