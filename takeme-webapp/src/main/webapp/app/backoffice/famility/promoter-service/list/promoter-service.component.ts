import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {Router} from '@angular/router';
import {IPromoterService} from '../promoter-service.model';
import {PromoterServiceService} from '../service/promoter-service.service';
import {PromoterItineraryService} from "../../promoter-itinerary/service/promoter-itinerary.service";
import {ErrorHandlerProviderService} from "../../../../shared/providers/error-handler-provider.service";
import {IPromoterItinerary} from "../../promoter-itinerary/promoter-itinerary.model";

@Component({
    selector: 'jhi-famility-promoter-service-list',
    templateUrl: './promoter-service.component.html',
})
export class PromoterServiceComponent implements OnInit, OnDestroy {
    services: IPromoterService[] = [];

    constructor(
        private promoterServiceService: PromoterServiceService,
        private promoterItineraryService: PromoterItineraryService,
        private errorHandler: ErrorHandlerProviderService,
        private router: Router
    ) {
    }

    ngOnInit(): void {
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
                    this.services.forEach((service: IPromoterService) => service.enrollmentURL = !service.enrollmentURL.startsWith("https://") ? ("https://" + service.enrollmentURL) : service.enrollmentURL)
                    this.fetchItineraries();
                },
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    private fetchItineraries() {
        this.services.forEach((service: IPromoterService) => {
            let request = {
                serviceId: service.id
            }

            this.promoterItineraryService
                .getPromoterItineraries(request)
                .subscribe({
                    next: (response: HttpResponse<IPromoterItinerary[]>) => service.itineraries = response.body,
                    error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                });
        });
    }

    goToServiceEdit(id) {
        this.router.navigate(['promoter/services', id, 'edit']).then();
    }

    goToItineraryDetail(serviceId, itineraryId) {
        this.router.navigate(['promoter/services', serviceId, 'promoter-itinerary', itineraryId]).then();
    }
}
