import {Component, OnInit, Input, OnDestroy} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {PromoterServiceService} from "../../../../famility/promoter-service/service/promoter-service.service";
import {PromoterItineraryService} from "../../../../famility/promoter-itinerary/service/promoter-itinerary.service";
import {IPromoterService} from "../../../../famility/promoter-service/promoter-service.model";
import {ChildrenService} from "../../../../../shared/services/children.service";
import dayjs from "dayjs/esm";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {ErrorHandlerProviderService} from "../../../../../shared/providers/error-handler-provider.service";
import {IPromoterItinerary} from "../../../../famility/promoter-itinerary/promoter-itinerary.model";
import {StatusType} from "../../../../../shared/model/child.model";
import {forkJoin, Observable} from "rxjs";

@Component({
    selector: 'jhi-tutor-passengers-list-subscription-dialog',
    templateUrl: './subscription-dialog.component.html'
})
export class SubscriptionDialogComponent implements OnInit, OnDestroy {
    @Input()
    child;

    services: IPromoterService[] = [];

    constructor(
        private promoterServiceService: PromoterServiceService,
        private promoterItineraryService: PromoterItineraryService,
        private childrenService: ChildrenService,
        private errorHandler: ErrorHandlerProviderService,
        private activeModal: NgbActiveModal
    ) {
    }

    ngOnInit() {
        this.fetchServices();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    private fetchServices() {
        this.promoterServiceService
            .getPromoterServices()
            .subscribe({
                next: (response: HttpResponse<IPromoterService[]>) => {
                    this.services = response.body;
                    this.fetchItineraries();
                },
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    private fetchItineraries() {
        this.services.forEach((service: IPromoterService) => {
            const requests = [this.getPromoterItinerariesWithoutEndDate(service.id), this.getPromoterItinerariesWithEndDateNotYetPassed(service.id)];
            forkJoin(requests)
                .subscribe({
                    next: (responses) => {
                        service.itineraries = responses[0].body;
                        service.itineraries = service.itineraries.concat(responses[1].body);
                    },
                    error: (error) => this.errorHandler.showError(error)
                });
        });
    }

    private getPromoterItinerariesWithoutEndDate(serviceId: number): Observable<HttpResponse<IPromoterItinerary[]>> {
        let params = {
            serviceId: serviceId,
            active: true
        };
        return this.promoterItineraryService.getPromoterItineraries(params);
    }

    private getPromoterItinerariesWithEndDateNotYetPassed(serviceId: number): Observable<HttpResponse<IPromoterItinerary[]>> {
        let params = {
            serviceId: serviceId,
            endDateHasNotPassed: true
        };
        return this.promoterItineraryService.getPromoterItineraries(params);
    }

    subscribe(itinerary, enrollmentURL) {
        let request = {
            statusType: StatusType.PENDING,
            subscriptionDate: dayjs(),
            child: this.child,
            promoterItinerary: itinerary
        }

        this.childrenService
            .createChildItinerarySubscription(request)
            .subscribe({
                next: () => this.activeModal.close(enrollmentURL),
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    dismiss() {
        this.activeModal.dismiss();
    }
}
