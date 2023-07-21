import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';
import { ItineraryDetailPage } from './itinerary-detail.page';
import { ItineraryStopPointModule } from '../itinerary-stop-point/itinerary-stop-point.module';
import { SharedModule } from 'src/app/shared/shared.module';

const routes: Routes = [
    {
        path: '',
        component: ItineraryDetailPage
    }
];

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        IonicModule,
        RouterModule.forChild(routes),
        ItineraryStopPointModule,
        SharedModule
    ],
    declarations: [ItineraryDetailPage]
})
export class ItineraryDetailPageModule { }
