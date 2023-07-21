import {Component, OnDestroy, OnInit} from '@angular/core';
import {TutorsService} from "../../../../shared/services/tutors.service";
import {HttpErrorResponse, HttpParams, HttpResponse} from "@angular/common/http";
import {Principal} from "../../../../core";
import {IItineraryStopPointChildView} from "../../../../shared/model/itinerary-stop-point-shild-view.model";
import {TotalElementsHeader} from "../../../../shared/util/total-elements-header";
import {ILocation} from "../../../../entities/location/location.model";
import {BuildAddress} from "../../../../shared/util/build-address";
import {ErrorHandlerProviderService} from "../../../../shared/providers/error-handler-provider.service";
import {IChild} from "../../../../shared/model/child.model";

@Component({
    selector: 'jhi-tutor-itineraries-list',
    templateUrl: './itineraries.component.html',
    providers: [TutorsService]
})
export class ItinerariesComponent implements OnInit, OnDestroy {
    itineraries: IItineraryStopPointChildView[] = [];

    childrenIds: number[] = [];

    totalItems = 0;
    page = 0;
    itemsPerPage = 10;

    constructor(
        private tutorsService: TutorsService,
        private errorHandler: ErrorHandlerProviderService,
        private principal: Principal,
        private totalElementsHeader: TotalElementsHeader
    ) {
    }

    ngOnInit() {
        this.fetchItineraries();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    private fetchItineraries() {
        this.tutorsService
            .getChildren(this.principal.getUserIdentity().tutor.id)
            .subscribe({
                next: (response: HttpResponse<IChild[]>) => {
                    response.body.forEach((child: IChild) => this.childrenIds.push(child.id))

                    const params: HttpParams = new HttpParams()
                        .set('childId', this.childrenIds.join(','))
                        .set('page', this.page)
                        .set('size', this.itemsPerPage);

                    this.tutorsService
                        .getByChildId(params)
                        .subscribe({
                            next: (response: HttpResponse<IItineraryStopPointChildView[]>) => {
                                this.itineraries = response.body;
                                this.totalItems = this.totalElementsHeader.getTotalElements(response.headers);
                            },
                            error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                        });
                },
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    buildAddress(location: ILocation): string {
        return BuildAddress.buildAddressFromLocationObject(location).replace(/["]/g, '');
    }

    navigateToPage($event: number) {
        this.page = $event - 1;
        this.fetchItineraries();
    }
}
