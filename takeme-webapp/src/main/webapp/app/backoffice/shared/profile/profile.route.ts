import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core';

import { ProfileComponent } from './profile.component';

export const profileRoute: Routes = [
    {
        path: 'profile',
        component: ProfileComponent,
        data: {
            authorities: ['ROLE_FAMILITY', 'ROLE_BUS_COMPANY', 'ROLE_TUTOR'],
            pageTitle: 'profile.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
