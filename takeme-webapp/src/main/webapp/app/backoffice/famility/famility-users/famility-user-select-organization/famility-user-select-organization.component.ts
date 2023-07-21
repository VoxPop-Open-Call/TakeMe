import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { BuildAddress } from 'app/shared/util/build-address';
import { OrganizationsService } from 'app/shared/services/organizations.service';
import { TotalElementsHeader } from 'app/shared/util/total-elements-header';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { Organization, StatusType } from 'app/shared/model/organization.model';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { debounceTime, Subject } from "rxjs";
import { DEBOUNCE_TIME } from "../../../../config/input.constants";

@Component({
    selector: 'jhi-famility-user-select-organization',
    templateUrl: './famility-user-select-organization.component.html',
    styles: []
})
export class FamilityUserSelectOrganizationComponent implements OnInit, OnDestroy {
    @Input()
    organizationSelected;

    @Input()
    organizationType;

    listOrganization: Organization[] = [];

    nameFilter = '';
    page = 0;
    pageSize = 20;
    totalElements = 0;

    textSearchUpdate = new Subject<string>();

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private activeModal: NgbActiveModal,
        private totalElementsHeader: TotalElementsHeader,
        private organizationsService: OrganizationsService
    ) {
        this.textSearchUpdate.pipe(debounceTime(DEBOUNCE_TIME)).subscribe(() => this.loadListOrganizations())
    }

    ngOnInit() {
        this.loadListOrganizations();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadListOrganizations() {
        this.organizationsService
            .getOrganizationsByType(this.nameFilter, this.organizationType, StatusType.ACTIVE, this.page, this.pageSize)
            .subscribe(
                (result: HttpResponse<Organization[]>) => {
                    this.listOrganization = result.body;
                    this.totalElements = this.totalElementsHeader.getTotalElements(result.headers);
                },
                (error: HttpErrorResponse) => {
                    this.errorHandler.showError(error);
                }
            );
    }

    newSelection(organization) {
        if (this.organizationSelected.id === organization.id) {
            this.organizationSelected = null;
        } else {
            this.organizationSelected = organization;
        }

        this.organizationSelected = organization;
    }

    close() {
        this.activeModal.close(this.organizationSelected);
    }

    dismiss() {
        this.activeModal.dismiss();
    }

    changePage(newPage) {
        this.page = newPage - 1;
        this.loadListOrganizations();
    }

    buildAddress(location): string {
        return BuildAddress.buildAddressFromLocationObject(location);
    }
}
