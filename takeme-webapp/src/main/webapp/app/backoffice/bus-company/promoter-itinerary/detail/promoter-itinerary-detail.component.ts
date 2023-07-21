import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import {IPromoterItinerary} from '../promoter-itinerary.model';

import {BuildAddress} from 'app/shared/util/build-address';
import {ILocation} from "../../../../shared/model/location.model";
import {OperatorPromoterItineraryService} from "../service/promoter-itinerary.service";
import {PromoterStopPointService} from "../../../famility/promoter-stop-point/service/promoter-stop-point.service";
import {LocationService} from 'app/entities/location/service/location.service';
import {IPromoterStopPoint} from "../../../famility/promoter-stop-point/promoter-stop-point.model";
import {IPromoterService, TransportType} from "../../../famility/promoter-service/promoter-service.model";

@Component({
  selector: 'jhi-promoter-itinerary-detail',
  templateUrl: './promoter-itinerary-detail.component.html',
})
export class PromoterItineraryDetailComponent implements OnInit {
    promoterItinerary: IPromoterItinerary | null = null;

    googleMapsURL;

    constructor(
      protected promoterItineraryService: OperatorPromoterItineraryService,
      protected promoterStopPointService: PromoterStopPointService,
      protected locationService: LocationService,
      private router: Router,
      protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit(): void {
        this.activatedRoute.data.subscribe(({ promoterItinerary }) => {
            this.promoterItinerary = this.promoterItineraryService.convertDateFromServer(promoterItinerary);
            this.loadStopPoints();
        });
    }

    loadStopPoints() {
        this.promoterStopPointService
            .getPromoterStopPoints(this.promoterItinerary.id)
            .subscribe(promoterStopPoints => {
                this.promoterItinerary.promoterItineraryStopPoints = promoterStopPoints.body;

                let processedLocations = promoterStopPoints.body.length;
                this.promoterItinerary.promoterItineraryStopPoints.forEach(stopPoint => {
                    this.locationService.find(stopPoint.location.id).subscribe({
                        next: res => {
                            stopPoint.location = res.body;

                            processedLocations--;
                            if (processedLocations == 0) {
                                this.googleMapsURL = this.setGoogleMapsURL();
                            }
                        }
                    })
                })
            });
    }

    previousState(): void {
        window.history.back();
    }

    buildAddress(location: ILocation | undefined): string | '' {
        return BuildAddress.buildAddressFromLocationObject(location);
    }

    goEdit(){
      this.router.navigate(['operator/promoter-itineraries/', this.promoterItinerary.id , 'edit']);
    }

    private setGoogleMapsURL(): string {
        let coordinates: { longitude: string, latitude: string }[] = [];
        this.promoterItinerary.promoterItineraryStopPoints.forEach((stopPoint: IPromoterStopPoint) => {
            let {location} = stopPoint;
            if (location) {
                let {longitude, latitude} = location as ILocation;
                coordinates.push({longitude, latitude});
            }
        })
        let transportType: TransportType = (this.promoterItinerary.service as IPromoterService).transportType;
        let travelMode = transportType == TransportType.CAR || transportType == TransportType.BUS ? "DRIVING" : transportType;
        return this.locationService.generateGoogleMapsURL(coordinates, travelMode);
    }
}
