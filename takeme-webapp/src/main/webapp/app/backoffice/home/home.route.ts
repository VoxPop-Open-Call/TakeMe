import { Route } from '@angular/router';

import { HomeComponent } from './home.component';
import { UserRouteAccessService } from 'app/core';

export const backofficeHomeRoute: Route = {
    path: 'famility',
    component: HomeComponent,
    data: {
        authorities: ['ROLE_FAMILITY', 'ROLE_BUS_COMPANY', 'ROLE_TUTOR'],
        pageTitle: 'backoffice.title'
    },
    canActivate: [UserRouteAccessService]
};
