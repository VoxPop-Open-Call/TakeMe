import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { OrganizationsService } from 'app/shared/services/organizations.service';
import { TotalElementsHeader } from 'app/shared/util/total-elements-header';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { Driver } from 'app/shared/model/driver.model';
import {debounceTime, Subject} from "rxjs";
import {DEBOUNCE_TIME} from "../../../../config/input.constants";

@Component({
    selector: 'jhi-famility-user-driver-select-dialog',
    templateUrl: './famility-user-driver-select-dialog.component.html',
    styles: []
})
export class FamilityUserDriverSelectDialogComponent implements OnInit, OnDestroy {
    @Input()
    driverSelected;

    @Input()
    organizationId;

    nameFilter = '';
    page = 0;
    pageSize = 20;
    totalElements = 0;
    listDrivers: Driver[] = [];

    textSearchUpdate = new Subject<string>();

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private activeModal: NgbActiveModal,
        private organizationsService: OrganizationsService,
        private totalElementsHeader: TotalElementsHeader
    ) {
        this.textSearchUpdate.pipe(debounceTime(DEBOUNCE_TIME)).subscribe(() => this.loadDriverList())
    }

    ngOnInit() {
        this.loadDriverList();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadDriverList() {
        this.organizationsService.getDriversWithoutUser(this.organizationId, this.nameFilter, this.page, this.pageSize).subscribe(
            (result: HttpResponse<Driver[]>) => {
                this.listDrivers = result.body;
                this.totalElements = this.totalElementsHeader.getTotalElements(result.headers);
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    newSelection(driver) {
        if (this.driverSelected) {
            if (this.driverSelected.id === driver.id) {
                this.driverSelected = null;
            } else {
                this.driverSelected = driver;
            }

            this.driverSelected = driver;
        } else {
            this.driverSelected = driver;
        }
    }

    changePage(newPage) {
        this.page = newPage - 1;
        this.loadDriverList();
    }

    close() {
        this.activeModal.close(this.driverSelected);
    }

    dismiss() {
        this.activeModal.dismiss();
    }
}
