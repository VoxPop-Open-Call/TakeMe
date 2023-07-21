import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import moment from 'moment';

import { Account } from 'app/core';
import { map } from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class UserService {
    constructor(private http: HttpClient) {}

    createWithMultipleRoles(user): Observable<HttpResponse<Account>> {
        return this.http.post<Account>(`api/account/create`, user, { observe: 'response' });
    }

    getAll(organizationId, firstName, lastName, active, page, size): Observable<HttpResponse<Account[]>> {
        return this.http.get<Account[]>(
            `api/users?organizationId=${organizationId}&firstName=${firstName}&lastName=${lastName}&active=${active}&page=${page}&size=${size}`,
            { observe: 'response' }
        );
    }

    patchProfileUser(id, patchProfile): Observable<any> {
        return this.http.patch(`api/users/${id}/profile`, patchProfile, { observe: 'response' });
    }

    getByEmail(email): Observable<HttpResponse<Account>> {
        return this.http
            .get<Account>(`api/users/${email}`, { observe: 'response' })
            .pipe(map((res: HttpResponse<Account>) => this.convertDateFromServer(res)));
    }

    patchStatus(id, activated): Observable<any> {
        const data = { activated };
        return this.http.patch(`api/users/${id}/update-status`, data, { observe: 'response' });
    }

    patchUser(id, firstName, lastName) {
        const data = { firstName, lastName };
        return this.http.patch(`api/users/${id}`, data, { observe: 'response' });
    }

    protected convertDateFromServer(res: HttpResponse<Account>): HttpResponse<Account> {
        if (res.body) {
            res.body.statusChangeDate = res.body.statusChangeDate != null ? moment(res.body.statusChangeDate) : null;
        }

        return res;
    }
}
