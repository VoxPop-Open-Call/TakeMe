import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LocationLinkComponent } from './location-link.component';
import { IonicModule } from '@ionic/angular';

@NgModule({
  imports: [
      CommonModule,
      IonicModule
  ],
  declarations: [LocationLinkComponent],
  exports: [LocationLinkComponent]
})
export class LocationLinkModule { }
