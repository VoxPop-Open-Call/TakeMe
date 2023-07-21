import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { ReportsComponent } from './reports.component';

export const reportsRoute: Routes = [
    {
        path: 'famility/reports',
        component: ReportsComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'backoffice.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
