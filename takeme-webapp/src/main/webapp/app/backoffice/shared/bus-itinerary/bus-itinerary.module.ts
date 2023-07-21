import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgxPaginationModule } from 'ngx-pagination';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { ReactiveFormsModule } from '@angular/forms';
import { TextMaskModule } from 'angular2-text-mask';
import { FamilityBackofficeSharedModule } from 'app/shared';

import { BusItineraryComponent } from './bus-itinerary.component';

import { busItineraryRoute } from './bus-itinerary.route';
import { BusItineraryDetailComponent } from './bus-itinerary-detail.component';
import { BusItineraryUpdateComponent } from './bus-itinerary-update.component';
import { DriverDialogComponent } from './driver/driver-dialog/driver-dialog.component';
import { VehicleDialogComponent } from './vehicle/vehicle-dialog/vehicle-dialog.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LocationDetailDialogComponent } from 'app/backoffice/shared/bus-itinerary/location/location-detail-dialog.component';
import { LocationSelectDialogComponent } from 'app/backoffice/shared/bus-itinerary/location/location-select-dialog.component';
import { UpdateLocationDialogComponent } from 'app/backoffice/shared/bus-itinerary/location/update-location-dialog.component';
import { StopPointSelectDialogComponent } from './stop-point/stop-point-select-dialog.component';
import { ErrorNoAllChildCompletePopupComponent } from './error-no-all-child-complete-popup/error-no-all-child-complete-popup.component';
import { BusItineraryDialogComponent } from './bus-itinerary-dialog/bus-itinerary-dialog.component';
import { TableModule } from "primeng/table";
import { ErrorDialogComponent } from "./error-dialog/error-dialog.component";

const ROUTES = [...busItineraryRoute];

@NgModule({
  imports: [
    CommonModule,
    FamilityBackofficeSharedModule,
    NgxPaginationModule,
    NgbModule,
    DragDropModule,
    ReactiveFormsModule,
    RouterModule.forChild(ROUTES),
    TextMaskModule,
    TableModule
  ],
    declarations: [
        BusItineraryComponent,
        BusItineraryDetailComponent,
        BusItineraryUpdateComponent,
        DriverDialogComponent,
        VehicleDialogComponent,
        LocationDetailDialogComponent,
        LocationSelectDialogComponent,
        UpdateLocationDialogComponent,
        BusItineraryComponent,
        BusItineraryDetailComponent,
        BusItineraryUpdateComponent,
        StopPointSelectDialogComponent,
        ErrorNoAllChildCompletePopupComponent,
        BusItineraryDialogComponent,
        ErrorDialogComponent
    ],
    entryComponents: [
        DriverDialogComponent,
        VehicleDialogComponent,
        LocationDetailDialogComponent,
        LocationSelectDialogComponent,
        UpdateLocationDialogComponent,
        StopPointSelectDialogComponent,
        ErrorNoAllChildCompletePopupComponent,
        BusItineraryDialogComponent,
        ErrorDialogComponent
    ]
})
export class BusItineraryModule {}
