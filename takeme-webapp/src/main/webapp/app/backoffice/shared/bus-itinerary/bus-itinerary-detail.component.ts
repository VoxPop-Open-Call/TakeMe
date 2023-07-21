import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { BuildAddress } from 'app/shared/util/build-address';
import { ItineraryService } from 'app/shared/services/itinerary.service';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { Itinerary } from 'app/shared/model/itinerary.model';
import {Location} from 'app/shared/model/location.model';
import { ItineraryStopPoint } from 'app/shared/model/itinerary-stop-point.model';
import { StopPointType } from "../../../shared/model/stop-point.model";
import { Principal } from "../../../core";
import { Authority } from "../../../config/authority.constants";
import {IPromoterService, TransportType} from "../../famility/promoter-service/promoter-service.model";
import {LocationService} from "../../../entities/location/service/location.service";

@Component({
    selector: 'jhi-bus-itinerary-detail',
    templateUrl: './bus-itinerary-detail.component.html',
    providers: [ItineraryService],
    styles: []
})
export class BusItineraryDetailComponent implements OnInit, OnDestroy {
    stopPointType = StopPointType;

    itineraryId: number;
    itinerary: Itinerary;
    itineraryDurationInSeconds: number;

    isOperator = false;

    googleMapsURL;

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private itineraryService: ItineraryService,
        private principal: Principal,
        protected locationService: LocationService
    ) {}

    ngOnInit() {
        this.itineraryId = this.activatedRoute.snapshot.params['id'];

        this.principal.hasAuthority(Authority.OPERATOR).then(result => {
            this.isOperator = result;

            this.loadItinerary();
        });
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadItinerary() {
        this.itineraryService.get(this.itineraryId).subscribe(
            (result: HttpResponse<Itinerary>) => {
                this.itinerary = result.body;
                if (this.itinerary.effectiveStartTime && this.itinerary.effectiveEndTime) {
                    this.itineraryDurationInSeconds = this.itinerary.effectiveEndTime.diff(this.itinerary.effectiveStartTime) / 1000;
                }
                this.itinerary.itineraryStopPointList.sort((iti1: ItineraryStopPoint, iti2: ItineraryStopPoint) => {
                    if (iti1.order < iti2.order) {
                        return -1;
                    } else if (iti1.order > iti2.order) {
                        return 1;
                    } else {
                        return 0;
                    }
                });
                this.googleMapsURL = this.setGoogleMapsURL();
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    goBack() {
        window.history.back();
    }

    createByTemplate() {
        this.router.navigate(['operator/itineraries', this.itineraryId, 'copy']);
    }

    buildAddress(location: Location): string {
        return BuildAddress.buildAddressFromLocationObject(location);
    }

    convertToTime(timeInSeconds: number): string {
        const hours = Math.floor(timeInSeconds / 3600);
        const minutes = Math.round((timeInSeconds % 3600) / 60);
        return hours + 'h ' + minutes.toString().padStart(2, '0') + 'm';
    }

    goEdit() {
        this.router.navigate(['operator/itineraries', this.itineraryId, 'edit']);
    }

    roundTotalDistance(distance) {
        return distance.toFixed(1);
    }

    private setGoogleMapsURL(): string {
        let coordinates: { longitude: string, latitude: string }[] = [];
        this.itinerary.itineraryStopPointList.forEach((stopPoint: ItineraryStopPoint) => {
            let {longitude, latitude} = stopPoint.stopPoint.location as Location;
            coordinates.push({longitude, latitude});
        })
        let transportType: TransportType = (this.itinerary.promoterItinerary.service as IPromoterService).transportType;
        let travelMode = transportType == TransportType.CAR || transportType == TransportType.BUS ? "driving" : transportType.toLowerCase();
        return this.locationService.generateGoogleMapsURL(coordinates, travelMode);
    }
}
