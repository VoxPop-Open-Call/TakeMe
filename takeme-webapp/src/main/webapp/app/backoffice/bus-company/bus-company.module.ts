import { NgModule } from '@angular/core';

import { SubscriptionsBusCompanyModule } from '../shared/subscriptions/subscriptions.module';
import { ChildrenModule } from '../shared/children/children.module';

import { SubscriptionFilterService } from 'app/shared/providers/subscription-filter.service';
import { VehiclesModule } from './vehicles/vehicles.module';
import { DriversModule } from './drivers/drivers.module';
import { ChildrenImportModule } from 'app/backoffice/shared/children-import/children-import.module';
import { PromoterItineraryModule } from "./promoter-itinerary/promoter-itinerary.module";

@NgModule({
    imports: [SubscriptionsBusCompanyModule, ChildrenModule, VehiclesModule, DriversModule, ChildrenImportModule, PromoterItineraryModule],
    providers: [SubscriptionFilterService],
    declarations: []
})
export class BusCompanyModule {}
