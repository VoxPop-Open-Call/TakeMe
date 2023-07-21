import { subscriptionsRoute } from './route/subscriptions.route';
import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { RouterModule } from '@angular/router';
import { FamilityBackofficeSharedModule } from 'app/shared';
import { SubscriptionsComponent } from "./list/subscriptions.component";
import { SubscriptionDetailComponent } from "./detail/subscription-detail.component";
import { SubscriptionDialogComponent } from "./list/subscription-dialog/subscription-dialog.component";
import { AdditionalInformationDialogComponent } from "./detail/additional-information-dialog/additional-information-dialog.component";
import { NgxPaginationModule } from "ngx-pagination";
import { ButtonModule } from "primeng/button";
import { SharedModule } from "primeng/api";
import { TableModule } from "primeng/table";
import { CommentDialogComponent } from "./list/comment-dialog/comment-dialog.component";

const ROUTES = [...subscriptionsRoute];

@NgModule({
    imports: [CommonModule, FormsModule, NgxPaginationModule, RouterModule.forChild(ROUTES), FamilityBackofficeSharedModule, ButtonModule, SharedModule, TableModule],
    declarations: [
        SubscriptionsComponent,
        SubscriptionDetailComponent,
        SubscriptionDialogComponent,
        AdditionalInformationDialogComponent,
        CommentDialogComponent
    ],
    exports: [SubscriptionsComponent]
})
export class SubscriptionsBusCompanyModule {
}
