import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { CurrentItineraryPage } from './current-itinerary.page';
import { CurrentItineraryStopPointComponent } from './current-itinerary-stop-point/current-itinerary-stop-point.component';
import { SharedModule } from '../shared/shared.module';

const routes: Routes = [
    {
        path: '',
        component: CurrentItineraryPage
    }
];

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        IonicModule,
        RouterModule.forChild(routes),
        SharedModule
    ],
    declarations: [CurrentItineraryPage, CurrentItineraryStopPointComponent]
})
export class CurrentItineraryPageModule { }
