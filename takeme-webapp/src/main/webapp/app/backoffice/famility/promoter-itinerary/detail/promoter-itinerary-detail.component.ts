import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {IPromoterItinerary} from '../promoter-itinerary.model';
import {BuildAddress} from 'app/shared/util/build-address';
import {ILocation} from "../../../../shared/model/location.model";
import {PromoterItineraryService} from "../service/promoter-itinerary.service";
import {PromoterStopPointService} from "../../promoter-stop-point/service/promoter-stop-point.service";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {IPromoterStopPoint} from "../../promoter-stop-point/promoter-stop-point.model";
import {ErrorHandlerProviderService} from "../../../../shared/providers/error-handler-provider.service";

@Component({
    selector: 'jhi-promoter-itinerary-detail',
    templateUrl: './promoter-itinerary-detail.component.html',
})
export class PromoterItineraryDetailComponent implements OnInit, OnDestroy {
    promoterItinerary: IPromoterItinerary | null = null;

    constructor(
        protected promoterItineraryService: PromoterItineraryService,
        protected promoterStopPointService: PromoterStopPointService,
        private errorHandler: ErrorHandlerProviderService,
        protected activatedRoute: ActivatedRoute
    ) {
    }

    ngOnInit(): void {
        this.activatedRoute.data.subscribe(({promoterItinerary}) => {
            this.promoterItinerary = promoterItinerary;
            this.loadStopPoints();
        });
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadStopPoints() {
        this.promoterStopPointService
            .getPromoterStopPoints(this.promoterItinerary.id)
            .subscribe({
                next: (response: HttpResponse<IPromoterStopPoint[]>) => this.promoterItinerary.promoterItineraryStopPoints = response.body,
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    previousState(): void {
        window.history.back();
    }

    buildAddress(location: ILocation | undefined): string | '' {
        return BuildAddress.buildAddressFromLocationObject(location);
    }
}
