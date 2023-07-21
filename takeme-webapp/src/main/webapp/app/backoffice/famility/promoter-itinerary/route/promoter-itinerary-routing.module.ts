import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { PromoterItineraryComponent } from '../list/promoter-itinerary.component';
import { PromoterItineraryDetailComponent } from '../detail/promoter-itinerary-detail.component';
import { PromoterItineraryUpdateComponent } from '../../../bus-company/promoter-itinerary/update/promoter-itinerary-update.component';
import { PromoterItineraryRoutingResolveService } from './promoter-itinerary-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const promoterItineraryRoute: Routes = [
  {
    path: 'promoter/itinerary',
    component: PromoterItineraryComponent,
    data: {
      defaultSort: 'id,' + ASC,
      authorities: ['ROLE_FAMILITY'],
      pageTitle: 'familityBackofficeApp.promoterItinerary.home.title'
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'promoter/promoter-itinerary/new',
    component: PromoterItineraryUpdateComponent,
    resolve: {
      promoterItinerary: PromoterItineraryRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_FAMILITY'],
      pageTitle: 'familityBackofficeApp.promoterItinerary.home.title'
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'promoter/services/:serviceId/promoter-itinerary/:id',
    component: PromoterItineraryDetailComponent,
    resolve: {
      promoterItinerary: PromoterItineraryRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_FAMILITY'],
      pageTitle: 'familityBackofficeApp.promoterItinerary.home.title'
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'promoter/promoter-itinerary/:id/edit',
    component: PromoterItineraryUpdateComponent,
    resolve: {
      promoterItinerary: PromoterItineraryRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_FAMILITY'],
      pageTitle: 'familityBackofficeApp.promoterItinerary.home.title'
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(promoterItineraryRoute)],
  exports: [RouterModule],
})
export class PromoterItineraryRoutingModule {}
