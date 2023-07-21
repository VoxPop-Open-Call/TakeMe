import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { VehicleListComponent } from 'app/backoffice/bus-company/vehicles/vehicle-list/vehicle-list.component';
import { UpdateVehicleComponent } from 'app/backoffice/bus-company/vehicles/update-vehicle/update-vehicle.component';

export const vehiclesRoute: Routes = [
    {
        path: 'operator/vehicles',
        component: VehicleListComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'familityBackofficeApp.vehicle.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'operator/vehicles/new',
        component: UpdateVehicleComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'familityBackofficeApp.vehicle.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'operator/vehicles/:id/edit',
        component: UpdateVehicleComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'familityBackofficeApp.vehicle.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
