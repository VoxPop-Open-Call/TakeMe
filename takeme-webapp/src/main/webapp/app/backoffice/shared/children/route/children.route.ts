import {Routes} from '@angular/router';
import {UserRouteAccessService} from 'app/core';
import {ChildrenComponent} from '../list/children.component';
import {ChildrenDetailComponent} from '../detail/children-detail.component';
import {ServicesComponent} from '../services/list/services.component';
import {ServiceUpdateComponent} from '../services/update/service-update.component';
import {ServiceDetailComponent} from '../services/detail/service-detail.component';

export const childrenRoutes: Routes = [
    {
        path: 'operator/passengers',
        component: ChildrenComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'backoffice.home.busCompany.children.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'operator/passengers/:id',
        component: ChildrenDetailComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'backoffice.home.busCompany.children.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'operator/passengers/:id/services',
        component: ServicesComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'backoffice.home.busCompany.children.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'operator/passengers/:id/services/new',
        component: ServiceUpdateComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'backoffice.home.busCompany.children.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'operator/passengers/:id/services/:serviceId',
        component: ServiceDetailComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'backoffice.home.busCompany.children.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'operator/passengers/:id/services/:serviceId/edit',
        component: ServiceUpdateComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'backoffice.home.busCompany.children.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promoter/passengers',
        component: ChildrenComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'backoffice.home.busCompany.children.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promoter/passengers/:id',
        component: ChildrenDetailComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'backoffice.home.busCompany.children.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promoter/passengers/:id/services',
        component: ServicesComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'backoffice.home.busCompany.children.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promoter/passengers/:id/services/:serviceId',
        component: ServiceDetailComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'backoffice.home.busCompany.children.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
