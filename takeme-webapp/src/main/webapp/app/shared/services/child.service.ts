import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DATE_FORMAT } from 'app/shared';

import { OrganizationType } from 'app/shared/model/organization.model';
import { StatusType } from 'app/shared/model/child-subscription.model';

import { ChildSubscription } from 'app/shared/model/child-subscription.model';

@Injectable()
export class ChildService {
    constructor(private http: HttpClient) {}

    patchChildInformation(id, name, dateOfBirth): Observable<HttpResponse<any>> {
        const data = { name, dateOfBirth };
        return this.http.patch<any>(`api/children/${id}`, this.convertDateFromClient(data), { observe: 'response' });
    }

    protected convertDateFromClient(childUpdate) {
        const copy = Object.assign({}, childUpdate, {
            dateOfBirth:
                childUpdate.dateOfBirth != null && childUpdate.dateOfBirth.isValid() ? childUpdate.dateOfBirth.format(DATE_FORMAT) : null
        });
        return copy;
    }
}
