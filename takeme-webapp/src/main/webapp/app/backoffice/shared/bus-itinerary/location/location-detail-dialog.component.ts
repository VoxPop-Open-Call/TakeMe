import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { UpdateLocationDialogComponent } from './update-location-dialog.component';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { LocationsService } from 'app/shared/services/locations.service';

import { Location } from 'app/shared/model/location.model';

@Component({
    selector: 'jhi-location-detail-dialog',
    templateUrl: './location-detail-dialog.component.html',
    providers: [LocationsService],
    styles: []
})
export class LocationDetailDialogComponent implements OnInit, OnDestroy {
    @Input()
    locationId;

    location: Location;

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private modalService: NgbModal,
        private activeModal: NgbActiveModal,
        private locationsService: LocationsService
    ) {}

    ngOnInit() {
        this.loadLocation();
    }

    ngOnDestroy() {
        this.errorHandler.clean();
    }

    loadLocation() {
        this.locationsService.get(this.locationId).subscribe(
            (result: HttpResponse<Location>) => {
                this.location = result.body;
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    dismiss() {
        this.activeModal.dismiss();
    }

    close() {
        this.activeModal.close();
    }

    goUpdate() {
        const modal = this.modalService.open(UpdateLocationDialogComponent);

        modal.componentInstance.location = this.location;

        modal.result.then(
            (result: string) => {
                if (result === 'delete') {
                    this.close();
                } else {
                    this.loadLocation();
                }
            },
            () => {}
        );
    }
}
