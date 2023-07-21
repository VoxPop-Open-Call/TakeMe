import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { Principal } from 'app/core';
import { TotalElementsHeader } from 'app/shared/util/total-elements-header';
import { ItineraryService } from 'app/shared/services/itinerary.service';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { BuildAddress } from 'app/shared/util/build-address';

import { Itinerary, ItineraryStatusType } from 'app/shared/model/itinerary.model';
import { Location } from 'app/shared/model/location.model';
import { BusItineraryDialogComponent } from './bus-itinerary-dialog/bus-itinerary-dialog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OrganizationService } from "../../../entities/organization/organization.service";
import { IOrganization } from "../../../shared/model/organization.model";
import { debounceTime, Subject } from "rxjs";
import { DEBOUNCE_TIME } from "../../../config/input.constants";
import { Authority } from "../../../config/authority.constants";

@Component({
    selector: 'jhi-bus-itinerary',
    templateUrl: './bus-itinerary.component.html',
    providers: [ItineraryService],
    styles: []
})
export class BusItineraryComponent implements OnInit, OnDestroy {
    idOrganization;
    isOperator = true;

    ongoing = true;
    finished = false;
    nameFilter = '';
    organizationFilter = '';

    page = 0;
    pageSize = 20;
    totalElements = 0;
    filterItineraryStatus: ItineraryStatusType[] = [];
    listItineraries: Itinerary[] = [];
    organizations: IOrganization[] = [];

    textSearchUpdate = new Subject<string>();

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private router: Router,
        private principal: Principal,
        private totalElementsService: TotalElementsHeader,
        private itineraryService: ItineraryService,
        private organizationService: OrganizationService,
        private modalService: NgbModal,
        private activatedRoute: ActivatedRoute
    ) {
        this.textSearchUpdate.pipe(debounceTime(DEBOUNCE_TIME)).subscribe(() => this.filterUpdate());
    }

    ngOnInit() {
        this.principal.hasAuthority(Authority.OPERATOR).then(result => {
            this.isOperator = result;

            this.activatedRoute.queryParams.subscribe(res => {
                if (res.ongoing) {
                    if (res.ongoing == 'false') {
                        this.ongoing = false;
                        this.finished = true;
                    } else {
                        this.ongoing = true;
                        this.finished = false;
                    }
                }

                if (res.name) {
                    this.nameFilter = res.name;
                }

                if (res.operator && !this.isOperator) {
                    this.organizationFilter = res.operator;
                }

                if (res.page) {
                    this.page = res.page - 1;
                }

                this.idOrganization = this.principal.getIdOrganization();
                this.loadItineraries();

                if (!this.isOperator) {
                    this.organizationService.query().subscribe(res => {
                        this.organizations = res.body;
                    });
                }
            });
        });
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadItineraries() {
        if (this.ongoing) {
            this.filterItineraryStatus = [ItineraryStatusType.READY_TO_START, ItineraryStatusType.IN_PROGRESS];
        } else {
            this.filterItineraryStatus = [ItineraryStatusType.FINISHED, ItineraryStatusType.CANCELED];
        }

        let sortProperty = 'scheduledTime';
        let sortDirection = 'asc';
        if (this.finished) {
            sortProperty = 'effectiveStartTime';
            sortDirection = 'desc';
        }
        this.itineraryService
            .getByIdOrganization(
                this.isOperator ? this.idOrganization : this.organizationFilter,
                this.nameFilter,
                this.filterItineraryStatus,
                this.page,
                this.pageSize,
                sortProperty,
                sortDirection
            )
            .subscribe(
                (result: HttpResponse<Itinerary[]>) => {
                    this.totalElements = this.totalElementsService.getTotalElements(result.headers);
                    this.listItineraries = result.body;
                },
                (error: HttpErrorResponse) => {
                    this.errorHandler.showError(error);
                }
            );
    }

    showOngoingItinerary() {
        this.page = 0;
        this.ongoing = true;
        this.finished = false;
        this.filterUpdate();
    }

    showFinishedItinerary() {
        this.page = 0;
        this.ongoing = false;
        this.finished = true;
        this.filterUpdate();
    }

    buildAddress(location: Location): string {
        return BuildAddress.buildAddressFromLocationObject(location);
    }

    goDetailItinerary(itineraryId: number) {
        if (this.isOperator) {
            this.router.navigate(['operator/itineraries', itineraryId]).then();
        }else{
            this.router.navigate(['promoter/itineraries', itineraryId]).then();
        }
    }

    finishItinerary(itineraryId: number) {
        const modal = this.modalService.open(BusItineraryDialogComponent);

        modal.componentInstance.actionType = 'finish';

        modal.result.then(
            () =>
                this.itineraryService.patchStatus(itineraryId, ItineraryStatusType.FINISHED).subscribe(
                    () => {
                        this.filterUpdate();
                    },
                    (error: HttpErrorResponse) => {
                        this.errorHandler.showError(error);
                    }
                ),
            () => {}
        );
    }

    cancelItinerary(itineraryId: number) {
        const modal = this.modalService.open(BusItineraryDialogComponent);

        modal.componentInstance.actionType = 'cancel';

        modal.result.then(
            () =>
                this.itineraryService.patchStatus(itineraryId, ItineraryStatusType.CANCELED).subscribe(
                    () => {
                        this.filterUpdate();
                    },
                    (error: HttpErrorResponse) => {
                        this.errorHandler.showError(error);
                    }
                ),
            () => {}
        );
    }

    changePage(newPage: number) {
        this.page = newPage - 1;
        this.filterUpdate();
    }

    goAdd() {
        this.router.navigate(['operator/itineraries/new']).then();
    }

    private filterUpdate() {
        if (this.isOperator) {
            this.router.navigate(['operator/itineraries'], {
                queryParams: {
                    ongoing: this.ongoing,
                    name: this.nameFilter == '' ? null : this.nameFilter,
                    page: this.page + 1
                }
            }).then();
        } else {
            this.router.navigate(['promoter/itineraries'], {
                queryParams: {
                    ongoing: this.ongoing,
                    name: this.nameFilter == '' ? null : this.nameFilter,
                    operator: this.organizationFilter == '' ? null : this.organizationFilter,
                    page: this.page + 1
                }
            }).then();
        }
    }

    resetFilter() {
        this.nameFilter = '';
        this.organizationFilter = '';
        this.filterUpdate();
    }
}
