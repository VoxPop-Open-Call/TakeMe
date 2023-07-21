import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { OrganizationsService } from 'app/shared/services/organizations.service';
import { TotalElementsHeader } from 'app/shared/util/total-elements-header';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { IVehicle } from 'app/shared/model/vehicle.model';

@Component({
    selector: 'jhi-vehicles-list-dialog',
    templateUrl: './vehicles-list-dialog.component.html',
    styles: [],
    providers: [OrganizationsService]
})
export class VehiclesListDialogComponent implements OnInit, OnDestroy {
    @Input()
    idOrganization;

    vehicles: IVehicle[];
    page = 0;
    pageSize = 10;
    totalElements = 0;

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private activeModal: NgbActiveModal,
        private organizationsService: OrganizationsService,
        private totalElementsHeader: TotalElementsHeader
    ) {}

    ngOnInit() {
        this.loadVehicles();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadVehicles() {
        this.organizationsService.getVehicles(this.idOrganization, this.page, this.pageSize).subscribe(
            (result: HttpResponse<IVehicle[]>) => {
                this.vehicles = result.body;
                this.totalElements = this.totalElementsHeader.getTotalElements(result.headers);
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    changePage(newPage) {
        this.page = newPage - 1;
        this.loadVehicles();
    }

    close() {
        this.activeModal.close();
    }
}
