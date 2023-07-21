import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FamilityBackofficeSharedModule } from 'app/shared';
import { vehiclesRoute } from './vehicles.route';
import { VehicleListComponent } from './vehicle-list/vehicle-list.component';
import { UpdateVehicleComponent } from './update-vehicle/update-vehicle.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { ReactiveFormsModule } from '@angular/forms';
import { TableModule } from "primeng/table";

const ROUTES = [...vehiclesRoute];

@NgModule({
    imports: [CommonModule, RouterModule.forChild(ROUTES), FamilityBackofficeSharedModule, NgxPaginationModule, ReactiveFormsModule, TableModule],
    declarations: [VehicleListComponent, UpdateVehicleComponent]
})
export class VehiclesModule {}
