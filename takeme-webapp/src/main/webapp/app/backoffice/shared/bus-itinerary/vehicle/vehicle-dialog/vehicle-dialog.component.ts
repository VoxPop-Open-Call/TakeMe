import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { OrganizationsService } from 'app/shared/services/organizations.service';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { Vehicle } from 'app/shared/model/vehicle.model';

@Component({
    selector: 'jhi-vehicle-dialog',
    templateUrl: './vehicle-dialog.component.html',
    providers: [OrganizationsService],
    styles: []
})
export class VehicleDialogComponent implements OnInit, OnDestroy {
    @Input()
    organizationId;

    @Input()
    vehicleSelected: Vehicle;

    vehicles: Vehicle[];

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private activeModal: NgbActiveModal,
        private organizationService: OrganizationsService
    ) {}

    ngOnInit() {
        this.loadVehicles();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadVehicles() {
        // TODO Falou-se que a API não iria ser paginada, validar.
        this.organizationService.getVehicles(this.organizationId, 0, 100).subscribe(
            (result: HttpResponse<Vehicle[]>) => {
                this.vehicles = result.body;
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    selectedVehicle() {
        this.activeModal.close(this.vehicleSelected);
    }

    newSelection(vehicle: Vehicle) {
        this.vehicleSelected = vehicle;
    }
    dismiss() {
        this.activeModal.dismiss();
    }
}
