import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { driversRoute } from './drivers.route';
import { FamilityBackofficeSharedModule } from 'app/shared';
import { RouterModule } from '@angular/router';
import { DriverListComponent } from './driver-list/driver-list.component';
import { UpdateDriverComponent } from './update-driver/update-driver.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { ReactiveFormsModule } from '@angular/forms';
import { TableModule } from "primeng/table";

const ROUTES = [...driversRoute];

@NgModule({
    imports: [CommonModule, RouterModule.forChild(ROUTES), FamilityBackofficeSharedModule, NgxPaginationModule, ReactiveFormsModule, TableModule],
    declarations: [DriverListComponent, UpdateDriverComponent]
})
export class DriversModule {}
