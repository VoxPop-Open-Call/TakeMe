import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';

import { BusItineraryComponent } from './bus-itinerary.component';
import { BusItineraryDetailComponent } from './bus-itinerary-detail.component';
import { BusItineraryUpdateComponent } from './bus-itinerary-update.component';

export const busItineraryRoute: Routes = [
    {
        path: 'operator/itineraries',
        component: BusItineraryComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'backoffice.home.busCompany.itinerary.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'operator/itineraries/new',
        component: BusItineraryUpdateComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'backoffice.home.busCompany.itinerary.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'operator/itineraries/:id/copy',
        component: BusItineraryUpdateComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'backoffice.home.busCompany.itinerary.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'operator/itineraries/:id/edit',
        component: BusItineraryUpdateComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'backoffice.home.busCompany.itinerary.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'operator/itineraries/:id',
        component: BusItineraryDetailComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'backoffice.home.busCompany.itinerary.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promoter/itineraries',
        component: BusItineraryComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'backoffice.home.famility.itineraries'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promoter/itineraries/:id',
        component: BusItineraryDetailComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'backoffice.home.famility.itineraries'
        },
        canActivate: [UserRouteAccessService]
    },
];
