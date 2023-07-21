import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChildItinerarySubscriptionComponent } from '../list/child-itinerary-subscription.component';
import { ChildItinerarySubscriptionDetailComponent } from '../detail/child-itinerary-subscription-detail.component';
import { ChildItinerarySubscriptionUpdateComponent } from '../update/child-itinerary-subscription-update.component';
import { ChildItinerarySubscriptionRoutingResolveService } from './child-itinerary-subscription-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const childItinerarySubscriptionRoute: Routes = [
  {
    path: '',
    component: ChildItinerarySubscriptionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChildItinerarySubscriptionDetailComponent,
    resolve: {
      childItinerarySubscription: ChildItinerarySubscriptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChildItinerarySubscriptionUpdateComponent,
    resolve: {
      childItinerarySubscription: ChildItinerarySubscriptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChildItinerarySubscriptionUpdateComponent,
    resolve: {
      childItinerarySubscription: ChildItinerarySubscriptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(childItinerarySubscriptionRoute)],
  exports: [RouterModule],
})
export class ChildItinerarySubscriptionRoutingModule {}
