import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ChildItinerarySubscriptionComponent } from './list/child-itinerary-subscription.component';
import { ChildItinerarySubscriptionDetailComponent } from './detail/child-itinerary-subscription-detail.component';
import { ChildItinerarySubscriptionUpdateComponent } from './update/child-itinerary-subscription-update.component';
import { ChildItinerarySubscriptionDeleteDialogComponent } from './delete/child-itinerary-subscription-delete-dialog.component';
import { ChildItinerarySubscriptionRoutingModule } from './route/child-itinerary-subscription-routing.module';

@NgModule({
  imports: [SharedModule, ChildItinerarySubscriptionRoutingModule],
  declarations: [
    ChildItinerarySubscriptionComponent,
    ChildItinerarySubscriptionDetailComponent,
    ChildItinerarySubscriptionUpdateComponent,
    ChildItinerarySubscriptionDeleteDialogComponent,
  ],
})
export class ChildItinerarySubscriptionModule {}
