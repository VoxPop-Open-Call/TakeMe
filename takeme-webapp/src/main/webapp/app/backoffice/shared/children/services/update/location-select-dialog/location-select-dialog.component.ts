import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { LocationUpdateDialogComponent } from '../location-update-dialog/location-update-dialog.component';
import { LocationDetailDialogComponent } from '../location-detail-dialog/location-detail-dialog.component';
import { OrganizationsService } from 'app/shared/services/organizations.service';
import { ChildService } from 'app/shared/services/child.service';
import { TotalElementsHeader } from 'app/shared/util/total-elements-header';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';
import { BuildAddress } from 'app/shared/util/build-address';
import { ILocation } from 'app/shared/model/location.model';
import { debounceTime, Subject } from "rxjs";
import { DEBOUNCE_TIME } from "../../../../../../config/input.constants";

@Component({
    selector: 'jhi-bus-company-passengers-services-update-select-location-dialog',
    templateUrl: './location-select-dialog.component.html',
    providers: [OrganizationsService, ChildService]
})
export class LocationSelectDialogComponent implements OnInit, OnDestroy {
    @Input()
    organizationId: number;

    organizationLocations: ILocation[] = [];

    totalItems = 0;
    page = 0;
    itemsPerPage = 10;
    locationFilter = '';

    locationSelected: ILocation;

    textSearchUpdate = new Subject<string>();

    constructor(
        private organizationsService: OrganizationsService,
        private errorHandler: ErrorHandlerProviderService,
        private modalService: NgbModal,
        private activeModal: NgbActiveModal,
        private totalElementsHeader: TotalElementsHeader
    ) {
        this.textSearchUpdate.pipe(debounceTime(DEBOUNCE_TIME)).subscribe(() => this.fetchOrganizationLocations())
    }

    ngOnInit() {
        this.fetchOrganizationLocations();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    fetchOrganizationLocations() {
        let request = {
            locationName: this.locationFilter
        }

        let pagination = {
            page: this.page,
            itemsPerPage: this.itemsPerPage
        }

        this.organizationsService
            .getLocations(this.organizationId, request, pagination)
            .subscribe({
                next: (response: HttpResponse<ILocation[]>) => {
                    this.organizationLocations = response.body;
                    this.totalItems = this.totalElementsHeader.getTotalElements(response.headers);
                },
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    applyFilter() {
        this.page = 0;
        this.fetchOrganizationLocations();
    }

    resetFilter() {
        this.locationFilter = '';
        this.fetchOrganizationLocations();
    }

    goToDetailed(id) {
        const modal = this.modalService.open(LocationDetailDialogComponent);
        modal.componentInstance.locationId = id;
        modal.result.then(
            () => this.fetchOrganizationLocations(),
            () => {
            }
        );
    }

    buildAddress(location) {
        return BuildAddress.buildAddressFromLocationObject(location);
    }

    selectAddressCheckbox(location) {
        this.locationSelected = this.locationSelected && location.id === this.locationSelected.id ? null : location;
    }

    navigateToPage($event) {
        this.page = $event - 1;
        this.fetchOrganizationLocations();
    }

    dismiss() {
        this.activeModal.dismiss();
    }

    createAddress() {
        const modal = this.modalService.open(LocationUpdateDialogComponent);
        modal.result.then(
            () => this.fetchOrganizationLocations(),
            () => {
            }
        );
    }

    selectAddressConfirm() {
        this.activeModal.close(this.locationSelected);
    }
}
