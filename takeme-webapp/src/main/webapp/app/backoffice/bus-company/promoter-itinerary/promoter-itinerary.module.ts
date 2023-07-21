import { NgModule } from '@angular/core';
import { FamilityBackofficeSharedModule } from 'app/shared';
import { PromoterItineraryComponent } from './list/promoter-itinerary.component';
import { PromoterItineraryDetailComponent } from './detail/promoter-itinerary-detail.component';
import { PromoterItineraryUpdateComponent } from './update/promoter-itinerary-update.component';
import { PromoterItineraryDeleteDialogComponent } from './delete/promoter-itinerary-delete-dialog.component';
import { PromoterItineraryRoutingModule } from './route/promoter-itinerary-routing.module';
import { PromoterStopPointSelectDialogComponent } from './stop-point/promoter-stop-point-select-dialog.component';
import { ReactiveFormsModule } from "@angular/forms";
import { DragDropModule } from "@angular/cdk/drag-drop";
import {TableModule} from "primeng/table";

@NgModule({
    imports: [FamilityBackofficeSharedModule, PromoterItineraryRoutingModule, ReactiveFormsModule, DragDropModule, TableModule],
  declarations: [
    PromoterItineraryComponent,
    PromoterItineraryDetailComponent,
    PromoterItineraryUpdateComponent,
    PromoterItineraryDeleteDialogComponent,
    PromoterStopPointSelectDialogComponent,
  ],
})
export class PromoterItineraryModule {}
