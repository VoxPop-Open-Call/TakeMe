import { NgModule } from '@angular/core';
import { FamilityBackofficeSharedModule } from 'app/shared';
import { PromoterItineraryComponent } from './list/promoter-itinerary.component';
import { PromoterItineraryDetailComponent } from './detail/promoter-itinerary-detail.component';
import { PromoterItineraryRoutingModule } from './route/promoter-itinerary-routing.module';
import { ReactiveFormsModule } from "@angular/forms";

@NgModule({
  imports: [FamilityBackofficeSharedModule, PromoterItineraryRoutingModule, ReactiveFormsModule],
  declarations: [
    PromoterItineraryComponent,
    PromoterItineraryDetailComponent,
  ],
})
export class PromoterItineraryModule {}
