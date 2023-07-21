import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';

import { UsersComponent } from './users.component';

export const usersRoute: Routes = [
    {
        path: 'shared/users',
        component: UsersComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'backoffice.home.busCompany.users'
        },
        canActivate: [UserRouteAccessService]
    }
];
