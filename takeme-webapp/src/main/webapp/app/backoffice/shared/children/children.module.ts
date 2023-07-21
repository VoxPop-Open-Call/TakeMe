import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {NgxPaginationModule} from 'ngx-pagination';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {FamilityBackofficeSharedModule} from 'app/shared';
import {ChildrenComponent} from './list/children.component';
import {ChildrenDetailComponent} from './detail/children-detail.component';
import {ServicesComponent} from './services/list/services.component';
import {ServiceDetailComponent} from './services/detail/service-detail.component';
import {ServiceUpdateComponent} from './services/update/service-update.component';
import {LocationSelectDialogComponent} from './services/update/location-select-dialog/location-select-dialog.component';
import {LocationUpdateDialogComponent} from './services/update/location-update-dialog/location-update-dialog.component';
import {LocationDetailDialogComponent} from './services/update/location-detail-dialog/location-detail-dialog.component';
import {childrenRoutes} from './route/children.route';
import {ServiceDeleteDialog} from './services/update/service-delete-dialog/service-delete-dialog.component';
import {SharedModule} from "primeng/api";
import {TableModule} from "primeng/table";
import {AdditionalInformationDialogComponent} from "./detail/additional-information-dialog/additional-information-dialog.component";
import {CommentDialogComponent} from "./detail/comment-dialog/comment-dialog.component";

const ROUTES = [...childrenRoutes];

@NgModule({
    imports: [CommonModule, FormsModule, ReactiveFormsModule, NgxPaginationModule, NgbModule, RouterModule.forChild(ROUTES), FamilityBackofficeSharedModule, SharedModule, TableModule],
    declarations: [
        ChildrenComponent,
        ChildrenDetailComponent,
        ServicesComponent,
        ServiceDetailComponent,
        ServiceUpdateComponent,
        LocationSelectDialogComponent,
        LocationUpdateDialogComponent,
        LocationDetailDialogComponent,
        ServiceDeleteDialog,
        AdditionalInformationDialogComponent,
        CommentDialogComponent
    ]
})
export class ChildrenModule {
}
