import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { ItinerariesComponent } from '../list/itineraries.component';

export const itinerariesRoute: Routes = [
    {
        path: 'tutor/itineraries',
        component: ItinerariesComponent,
        data: {
            authorities: ['ROLE_TUTOR'],
            pageTitle: 'backoffice.home.tutors.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
