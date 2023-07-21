import { NgModule } from '@angular/core';
import { HomeModule } from './home/home.module';
import { FamilityModule } from './famility/famility.module';
import { BusCompanyModule } from './bus-company/bus-company.module';
import { TutorsModule } from "./tutor/tutor.module";
import { UsersModule } from "./shared/users/users.module";
import { ProfileModule } from "./shared/profile/profile.module";

@NgModule({
    imports: [FamilityModule, HomeModule, BusCompanyModule, TutorsModule, UsersModule, ProfileModule]
})
export class BackOfficeModule {
}
