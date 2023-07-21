import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HomeComponent } from './home.component';
import { backofficeHomeRoute } from './home.route';
import { SubscriptionsBusCompanyModule } from '../shared/subscriptions/subscriptions.module';
import { TutorsModule } from '../famility/tutors/tutors.module';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild([backofficeHomeRoute]),
    TutorsModule,
    SubscriptionsBusCompanyModule
  ],
    declarations: [HomeComponent]
})
export class HomeModule {}
