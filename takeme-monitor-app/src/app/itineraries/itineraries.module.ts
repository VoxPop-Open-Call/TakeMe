import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { ItinerariesPage } from './itineraries.page';
import { ItineraryComponent } from './itineraries/itinerary.component';



const routes: Routes = [
    {
        path: '',
        component: ItinerariesPage
    },
    {
        path: 'itinerary-details',
        loadChildren: './itinerary-detail/itinerary-detail.module#ItineraryDetailPageModule',
    }
];

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        IonicModule,
        RouterModule.forChild(routes)
    ],
    declarations: [ItinerariesPage, ItineraryComponent]
})
export class ItinerariesPageModule {
}
