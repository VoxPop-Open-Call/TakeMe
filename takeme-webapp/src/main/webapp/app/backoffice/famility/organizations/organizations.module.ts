import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgxPaginationModule } from 'ngx-pagination';

import { OrganizationsComponent } from './organizations.component';
import { OrganizationUpdateComponent } from './organization-update.component';
import { OrganizationDetailComponent } from './organization-detail.component';
import { OrganizationChangeStatusConfirmationDialogComponent } from './organization-change-status-confirmation-dialog.component';
import { DriversListDialogComponent } from './bus-company/drivers-list-dialog.component';
import { VehiclesListDialogComponent } from './bus-company/vehicles-list-dialog.component';

import { OrganizationsFilterService } from 'app/shared/providers/organizations-filter.service';

import { organizationsRoute } from './organizations.route';
import { FamilityBackofficeSharedModule } from 'app/shared';
import { TableModule } from "primeng/table";

const ROUTES = [...organizationsRoute];

@NgModule({
    imports: [CommonModule, FormsModule, NgxPaginationModule, RouterModule.forChild(ROUTES), FamilityBackofficeSharedModule, TableModule],
    declarations: [
        OrganizationsComponent,
        OrganizationUpdateComponent,
        OrganizationDetailComponent,
        OrganizationChangeStatusConfirmationDialogComponent,
        DriversListDialogComponent,
        VehiclesListDialogComponent
    ],
    providers: [OrganizationsFilterService],
    entryComponents: [OrganizationChangeStatusConfirmationDialogComponent, DriversListDialogComponent, VehiclesListDialogComponent]
})
export class OrganizationsModule {}
