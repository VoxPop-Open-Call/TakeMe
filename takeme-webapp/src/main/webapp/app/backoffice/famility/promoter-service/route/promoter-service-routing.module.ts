import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserRouteAccessService} from 'app/core';
import {PromoterServiceComponent} from '../list/promoter-service.component';
import {PromoterServiceUpdateComponent} from '../update/promoter-service-update.component';
import {PromoterServiceRoutingResolveService} from './promoter-service-routing-resolve.service';

const promoterServiceRoute: Routes = [
    {
        path: 'promoter/services',
        component: PromoterServiceComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'familityBackofficeApp.promoterService.home.title'
        },
        canActivate: [UserRouteAccessService],
    },
    {
        path: 'promoter/services/new',
        component: PromoterServiceUpdateComponent,
        resolve: {
            promoterService: PromoterServiceRoutingResolveService,
        },
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'familityBackofficeApp.promoterService.home.title'
        },
        canActivate: [UserRouteAccessService],
    },
    {
        path: 'promoter/services/:id/edit',
        component: PromoterServiceUpdateComponent,
        resolve: {
            promoterService: PromoterServiceRoutingResolveService,
        },
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'familityBackofficeApp.promoterService.home.title'
        },
        canActivate: [UserRouteAccessService],
    },
];

@NgModule({
    imports: [RouterModule.forChild(promoterServiceRoute)],
    exports: [RouterModule],
})
export class PromoterServiceRoutingModule {
}
