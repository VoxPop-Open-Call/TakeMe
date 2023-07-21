import { Routes } from "@angular/router";
import { SubscriptionsComponent } from "../list/subscriptions.component";
import { UserRouteAccessService } from "../../../../core";
import { SubscriptionDetailComponent } from "../detail/subscription-detail.component";

export const subscriptionsRoute: Routes = [
    {
        path: 'operator/subscriptions',
        component: SubscriptionsComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'backoffice.home.busCompany.subscription.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'operator/subscriptions/:id',
        component: SubscriptionDetailComponent,
        data: {
            authorities: ['ROLE_BUS_COMPANY'],
            pageTitle: 'backoffice.home.busCompany.subscription.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promoter/subscriptions',
        component: SubscriptionsComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'backoffice.home.busCompany.subscription.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promoter/subscriptions/:id',
        component: SubscriptionDetailComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'backoffice.home.busCompany.subscription.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
