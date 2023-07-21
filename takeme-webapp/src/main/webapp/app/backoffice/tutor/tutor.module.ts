import { NgModule } from '@angular/core';
import { ChildrenModule } from "./passengers/children.module";
import { SubscriptionDialogComponent } from "./passengers/list/subscription-dialog/subscription-dialog.component";
import { ButtonModule } from "primeng/button";
import { FamilityBackofficeSharedCommonModule, FamilityBackofficeSharedModule } from "../../shared";
import { NgIf } from "@angular/common";
import { RouterLinkWithHref } from "@angular/router";
import { SharedModule } from "primeng/api";
import { TableModule } from "primeng/table";
import { CommentDialogComponent } from "./passengers/list/comment-dialog/comment-dialog.component";
import { EnrollmentDialogComponent } from "./passengers/list/enrollment-dialog/enrollment-dialog.component";
import { ItinerariesModule } from "./itineraries/itineraries.module";

@NgModule({
    imports: [ChildrenModule, ButtonModule, FamilityBackofficeSharedCommonModule, FamilityBackofficeSharedModule, NgIf, RouterLinkWithHref, SharedModule, TableModule, ItinerariesModule],
    declarations: [
        SubscriptionDialogComponent,
        CommentDialogComponent,
        EnrollmentDialogComponent
    ]
})
export class TutorsModule {
}
