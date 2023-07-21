import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';

import { FamilityUsersComponent } from './famility-users.component';
import { FamilityUserUpdateComponent } from './famility-user-update.component';
import { FamilityUserDetailComponent } from './famility-user-detail.component';

export const familityUsersRoute: Routes = [
    {
        path: 'promoter/users',
        component: FamilityUsersComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'userManagement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promoter/users/new',
        component: FamilityUserUpdateComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'userManagement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promoter/users/:email',
        component: FamilityUserDetailComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'userManagement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
