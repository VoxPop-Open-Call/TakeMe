import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Organization } from 'app/shared/model/organization.model';
import { OrganizationService } from './organization.service';
import { OrganizationDetailComponent } from './organization-detail.component';
import { IOrganization } from 'app/shared/model/organization.model';

@Injectable({ providedIn: 'root' })
export class OrganizationResolve implements Resolve<IOrganization> {
    constructor(private service: OrganizationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Organization> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Organization>) => response.ok),
                map((organization: HttpResponse<Organization>) => organization.body)
            );
        }
        return of(new Organization());
    }
}

export const organizationRoute: Routes = [
    {
        path: 'organization/:id/view',
        component: OrganizationDetailComponent,
        resolve: {
            organization: OrganizationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'familityBackofficeApp.organization.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
];
