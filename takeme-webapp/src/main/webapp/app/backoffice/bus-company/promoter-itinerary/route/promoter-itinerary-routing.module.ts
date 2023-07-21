import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { PromoterItineraryComponent } from '../list/promoter-itinerary.component';
import { PromoterItineraryDetailComponent } from '../detail/promoter-itinerary-detail.component';
import { PromoterItineraryUpdateComponent } from '../update/promoter-itinerary-update.component';
import { OperatorPromoterItineraryRoutingResolveService } from './promoter-itinerary-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const promoterItineraryRoute: Routes = [
  {
    path: 'operator/promoter-itineraries',
    component: PromoterItineraryComponent,
    data: {
      defaultSort: 'id,' + ASC,
      pageTitle: 'familityBackofficeApp.promoterItinerary.home.title'
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'operator/promoter-itineraries/new',
    component: PromoterItineraryUpdateComponent,
    data: {
      pageTitle: 'familityBackofficeApp.promoterItinerary.home.title'
    },
    resolve: {
      promoterItinerary: OperatorPromoterItineraryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'operator/promoter-itineraries/:id',
    component: PromoterItineraryDetailComponent,
    data: {
      pageTitle: 'familityBackofficeApp.promoterItinerary.home.title'
    },
    resolve: {
      promoterItinerary: OperatorPromoterItineraryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'operator/promoter-itineraries/:id/edit',
    component: PromoterItineraryUpdateComponent,
    data: {
      pageTitle: 'familityBackofficeApp.promoterItinerary.home.title'
    },
    resolve: {
      promoterItinerary: OperatorPromoterItineraryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(promoterItineraryRoute)],
  exports: [RouterModule],
})
export class PromoterItineraryRoutingModule {}
