import { NgModule } from '@angular/core';
import { OrganizationsModule } from './organizations/organizations.module';
import { TutorsModule } from './tutors/tutors.module';

import { TutorsFilterService } from './tutors/tutors-filter.service';
import { ReportsModule } from './reports/reports.module';
import { FamilityUsersModule } from './famility-users/famility-users.module';
import { PromoterItineraryModule } from "./promoter-itinerary/promoter-itinerary.module";
import { PromoterServiceModule } from "./promoter-service/promoter-service.module";
import { PromoterStopPointModule } from "./promoter-stop-point/promoter-stop-point.module";
import { BusItineraryModule } from '../shared/bus-itinerary/bus-itinerary.module';

@NgModule({
  imports: [
    OrganizationsModule,
    TutorsModule,
    ReportsModule,
    FamilityUsersModule,
    PromoterItineraryModule,
    PromoterServiceModule,
    PromoterStopPointModule,
    BusItineraryModule,
  ],
  declarations: [],
  exports: [],
  providers: [TutorsFilterService]
})
export class FamilityModule {
}
