import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { ItineraryStopPointComponent } from './itinerary-stop-point.component';
import { IonicModule } from '@ionic/angular';

@NgModule({
  declarations: [ItineraryStopPointComponent],
  imports: [
    CommonModule,
    SharedModule,
    IonicModule
  ],
  exports : [ItineraryStopPointComponent]
})
export class ItineraryStopPointModule { }
