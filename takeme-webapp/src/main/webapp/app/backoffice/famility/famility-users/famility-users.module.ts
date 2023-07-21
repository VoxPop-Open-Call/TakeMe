import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgxPaginationModule } from 'ngx-pagination';
import { ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { FamilityBackofficeSharedModule } from 'app/shared';

import { FamilityUsersComponent } from './famility-users.component';
import { FamilityUserChangeStatusDialogComponent } from './famility-user-change-status-dialog/famility-user-change-status-dialog.component';
import { FamilityUserDetailComponent } from './famility-user-detail.component';
import { FamilityUserUpdateComponent } from './famility-user-update.component';
import { FamilityUserSelectOrganizationComponent } from './famility-user-select-organization/famility-user-select-organization.component';

import { familityUsersRoute } from './famility-users.route';
import { FamilityUserDriverSelectDialogComponent } from './famility-user-driver-select-dialog/famility-user-driver-select-dialog.component';
import { TableModule } from "primeng/table";

const ROUTES = [...familityUsersRoute];

@NgModule({
    imports: [
        CommonModule,
        RouterModule.forChild(ROUTES),
        FamilityBackofficeSharedModule,
        NgxPaginationModule,
        ReactiveFormsModule,
        NgbModule,
        TableModule
    ],
    declarations: [
        FamilityUsersComponent,
        FamilityUserChangeStatusDialogComponent,
        FamilityUserDetailComponent,
        FamilityUserUpdateComponent,
        FamilityUserSelectOrganizationComponent,
        FamilityUserDriverSelectDialogComponent
    ],
    entryComponents: [
        FamilityUserChangeStatusDialogComponent,
        FamilityUserSelectOrganizationComponent,
        FamilityUserDriverSelectDialogComponent
    ]
})
export class FamilityUsersModule {}
