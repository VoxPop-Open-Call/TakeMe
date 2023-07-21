import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { Driver } from 'app/shared/model/driver.model';
import { OrganizationsService } from 'app/shared/services/organizations.service';

@Component({
    selector: 'jhi-driver-dialog',
    templateUrl: './driver-dialog.component.html',
    providers: [OrganizationsService],
    styles: []
})
export class DriverDialogComponent implements OnInit, OnDestroy {
    @Input()
    organizationId;

    @Input()
    driverSelected: Driver;

    drivers: Driver[];

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private activeModal: NgbActiveModal,
        private organizationService: OrganizationsService
    ) {}

    ngOnInit() {
        this.loadDrivers();
    }

    ngOnDestroy() {
        this.errorHandler.clean();
    }

    loadDrivers() {
        // TODO Falou-se que a API não iria ser paginada, validar.
        this.organizationService.getDrivers(this.organizationId, 0, 100).subscribe(
            (result: HttpResponse<Driver[]>) => {
                this.drivers = result.body;
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    newSelection(driver: Driver) {
        this.driverSelected = driver;
    }

    dismiss() {
        this.activeModal.dismiss();
    }

    selectedDriver() {
        this.activeModal.close(this.driverSelected);
    }
}
