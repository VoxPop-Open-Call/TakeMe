import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { UpdateLocationDialogComponent } from './update-location-dialog.component';
import { LocationDetailDialogComponent } from './location-detail-dialog.component';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { OrganizationsService } from 'app/shared/services/organizations.service';
import { TotalElementsHeader } from 'app/shared/util/total-elements-header';
import { BuildAddress } from 'app/shared/util/build-address';

import { Location } from 'app/shared/model/location.model';

@Component({
    selector: 'jhi-location-select-dialog',
    templateUrl: './location-select-dialog.component.html',
    providers: [OrganizationsService]
})
export class LocationSelectDialogComponent implements OnInit, OnDestroy {
    @Input()
    organizationId: number;

    @Input()
    locationSelected: Location;

    locations: Location[] = [];

    designationFilter = '';
    page = 0;
    pageSize = 20;
    totalElements = 0;

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private modalService: NgbModal,
        private activeModal: NgbActiveModal,
        private organizationsService: OrganizationsService,
        private totalElementsHeader: TotalElementsHeader
    ) {}

    ngOnInit() {
        this.loadOrganizationAddresses();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadOrganizationAddresses() {
        let request = {
            locationName: this.designationFilter
        }

        let pagination = {
            page: this.page,
            itemsPerPage: this.pageSize
        }

        this.organizationsService.getLocations(this.organizationId, request, pagination).subscribe(
            (result: HttpResponse<Location[]>) => {
                this.totalElements = this.totalElementsHeader.getTotalElements(result.headers);
                this.locations = result.body;
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    newSelection(location: Location) {
        if (this.locationSelected && location.id === this.locationSelected.id) {
            this.locationSelected = null;
        } else {
            this.locationSelected = location;
        }
    }

    newAddress() {
        const modal = this.modalService.open(UpdateLocationDialogComponent);

        modal.result.then(
            () => {
                this.loadOrganizationAddresses();
            },
            () => {}
        );
    }

    goDetail(id) {
        const modal = this.modalService.open(LocationDetailDialogComponent);

        modal.componentInstance.locationId = id;

        modal.result.then(
            () => {
                this.loadOrganizationAddresses();
            },
            () => {}
        );
    }

    buildAddress(location: Location) {
        return BuildAddress.buildAddressFromLocationObject(location);
    }

    changePage(newPage) {
        this.page = newPage - 1;
        this.loadOrganizationAddresses();
    }

    dismiss() {
        this.activeModal.dismiss();
    }

    close() {
        this.activeModal.close(this.locationSelected);
    }
}
