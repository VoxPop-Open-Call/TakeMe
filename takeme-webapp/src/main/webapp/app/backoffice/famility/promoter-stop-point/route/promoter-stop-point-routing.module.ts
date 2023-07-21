import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { PromoterStopPointComponent } from '../list/promoter-stop-point.component';
import { PromoterStopPointDetailComponent } from '../detail/promoter-stop-point-detail.component';
import { PromoterStopPointUpdateComponent } from '../update/promoter-stop-point-update.component';
import { PromoterStopPointRoutingResolveService } from './promoter-stop-point-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const promoterStopPointRoute: Routes = [
  {
    path: 'promoter/stop-points',
    component: PromoterStopPointComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'promoter/stop-points/new',
    component: PromoterStopPointUpdateComponent,
    resolve: {
      promoterStopPoint: PromoterStopPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'promoter/stop-points/:id',
    component: PromoterStopPointDetailComponent,
    resolve: {
      promoterStopPoint: PromoterStopPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'promoter/stop-points/:id/edit',
    component: PromoterStopPointUpdateComponent,
    resolve: {
      promoterStopPoint: PromoterStopPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(promoterStopPointRoute)],
  exports: [RouterModule],
})
export class PromoterStopPointRoutingModule {}
