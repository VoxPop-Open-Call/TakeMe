import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IUser } from 'app/core';

@Injectable()
export class UsersService {
    constructor(private http: HttpClient) {}

    getUsersOrganization(
        idOrganization,
        firstNameFiler: string,
        lastNameFilter: string,
        page: number,
        pageSize: number
    ): Observable<HttpResponse<IUser[]>> {
        return this.http.get<IUser[]>(
            `api/users?organizationId=${idOrganization}&firstName=${firstNameFiler}&lastName=${lastNameFilter}&page=${page}&size=${pageSize}`,
            { observe: 'response' }
        );
    }

    updateUserStatus(idUser: number, activated: boolean) {
        const data = {
            activated
        };

        return this.http.patch(`api/users/${idUser}/update-status`, data, { observe: 'response' });
    }
}
