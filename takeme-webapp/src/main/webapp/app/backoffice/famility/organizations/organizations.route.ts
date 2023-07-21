import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';

import { OrganizationsComponent } from './organizations.component';
import { OrganizationUpdateComponent } from './organization-update.component';
import { OrganizationDetailComponent } from './organization-detail.component';

import { OrganizationType } from 'app/shared/model/organization.model';

export const organizationsRoute: Routes = [
    // Bus-company routes
    {
        path: 'promoter/operators',
        component: OrganizationsComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'familityBackofficeApp.organization.home.title',
            organizationType: OrganizationType.BUS_COMPANY
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promoter/operators/new',
        component: OrganizationUpdateComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'familityBackofficeApp.organization.home.title',
            organizationType: OrganizationType.BUS_COMPANY
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promoter/operators/:id',
        component: OrganizationDetailComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'familityBackofficeApp.organization.home.title',
            organizationType: OrganizationType.BUS_COMPANY
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promoter/operators/:id/edit',
        component: OrganizationUpdateComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'familityBackofficeApp.organization.home.title',
            organizationType: OrganizationType.BUS_COMPANY
        },
        canActivate: [UserRouteAccessService]
    }
];
