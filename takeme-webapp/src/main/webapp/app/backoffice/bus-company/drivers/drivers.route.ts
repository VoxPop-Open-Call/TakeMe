import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { DriverListComponent } from 'app/backoffice/bus-company/drivers/driver-list/driver-list.component';
import { UpdateDriverComponent } from 'app/backoffice/bus-company/drivers/update-driver/update-driver.component';

export const driversRoute: Routes = [
    {
        path: 'operator/monitors',
        component: DriverListComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'familityBackofficeApp.driver.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'operator/monitors/new',
        component: UpdateDriverComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'familityBackofficeApp.driver.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'operator/monitors/:id/edit',
        component: UpdateDriverComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'familityBackofficeApp.driver.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
