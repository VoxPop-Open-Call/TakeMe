import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { OrganizationsService } from 'app/shared/services/organizations.service';
import { TotalElementsHeader } from 'app/shared/util/total-elements-header';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { IDriver } from 'app/shared/model/driver.model';

@Component({
    selector: 'jhi-drivers-list-dialog',
    templateUrl: './drivers-list-dialog.component.html',
    styles: [],
    providers: [OrganizationsService]
})
export class DriversListDialogComponent implements OnInit, OnDestroy {
    @Input()
    idOrganization;

    drivers: IDriver[];
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
        this.organizationsService.getDrivers(this.idOrganization, this.page, this.pageSize).subscribe(
            (result: HttpResponse<IDriver[]>) => {
                this.drivers = result.body;
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
