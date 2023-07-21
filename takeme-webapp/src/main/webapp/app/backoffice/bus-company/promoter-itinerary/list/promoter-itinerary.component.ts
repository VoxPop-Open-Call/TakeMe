import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { IPromoterItinerary } from '../promoter-itinerary.model';
import { OperatorPromoterItineraryService } from '../service/promoter-itinerary.service';
import { Principal } from "../../../../core";
import { TotalElementsHeader } from "../../../../shared/util/total-elements-header";
import { ErrorHandlerProviderService } from "../../../../shared/providers/error-handler-provider.service";
import { ITEMS_PER_PAGE } from "../../../../config/pagination.constants";

@Component({
    selector: 'jhi-bus-company-list-promoter-itinerary',
    templateUrl: './promoter-itinerary.component.html',
})
export class PromoterItineraryComponent implements OnInit, OnDestroy {
    promoterItineraries: IPromoterItinerary[] = [];

    totalItems = 0;
    page = 0;
    itemsPerPage = ITEMS_PER_PAGE;
    sortProperty = 'id';
    sortDirection = 'asc';

    constructor(
        private promoterItineraryService: OperatorPromoterItineraryService,
        private errorHandler: ErrorHandlerProviderService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private totalElementsHeader: TotalElementsHeader
    ) {
    }

    ngOnInit(): void {
        this.fetchItineraries();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    private fetchItineraries(): void {
        let pagination = {
            page: this.page,
            itemsPerPage: this.itemsPerPage,
            sortProperty: this.sortProperty,
            sortDirection: this.sortDirection
        }

        this.promoterItineraryService
            .query(this.principal.getIdOrganization(), pagination)
            .subscribe({
                next: (response: HttpResponse<IPromoterItinerary[]>) => {
                    this.promoterItineraries = response.body;
                    this.totalItems = this.totalElementsHeader.getTotalElements(response.headers);
                },
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    goToDetailed(id) {
        this.router.navigate(['operator/promoter-itineraries', id]).then();
    }

    navigateToPage($event) {
        this.page = $event - 1;
        this.fetchItineraries();
    }
}
