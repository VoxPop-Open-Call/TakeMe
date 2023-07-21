import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OrganizationChangeStatusConfirmationDialogComponent } from './organization-change-status-confirmation-dialog.component';
import { OrganizationsService } from 'app/shared/services/organizations.service';
import { TotalElementsHeader } from 'app/shared/util/total-elements-header';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';
import { IOrganization, StatusType } from 'app/shared/model/organization.model';
import { OrganizationType } from 'app/shared/model/organization.model';
import { debounceTime, Subject } from "rxjs";
import { DEBOUNCE_TIME } from "../../../config/input.constants";

@Component({
    selector: 'jhi-famility-organizations-list',
    templateUrl: './organizations.component.html'
})
export class OrganizationsComponent implements OnInit, OnDestroy {
    operators: IOrganization[] = [];

    activeOperator = StatusType.ACTIVE;
    inactiveOperator = StatusType.INACTIVE;

    showActive = true;
    showInactive = false;

    operatorFilter = '';

    totalItems = 0;
    page = 0;
    itemsPerPage = 5;
    sortProperty = 'name';
    sortDirection = 'desc';

    textSearchUpdate = new Subject<string>();

    constructor(
        private organizationsService: OrganizationsService,
        private errorHandler: ErrorHandlerProviderService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private totalElementsHeader: TotalElementsHeader,
        private modalService: NgbModal
    ) {
        this.textSearchUpdate.pipe(debounceTime(DEBOUNCE_TIME)).subscribe(() => this.filterUpdate())
    }

    ngOnInit() {
        this.activatedRoute.queryParams.subscribe(params => {
            if (params.name) {
                this.operatorFilter = params.name;
            }

            if (params.active) {
                if (params.active == 'true') {
                    this.showActive = true;
                    this.showInactive = false;
                } else {
                    this.showActive = false;
                    this.showInactive = true;
                }
            }

            if (params.page) {
                this.page = params.page - 1;
            }

            this.fetchOperators();
        });
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    private fetchOperators() {
        let request = {
            operatorName: this.operatorFilter,
            operatorType: OrganizationType.BUS_COMPANY,
            operatorStatus: this.showActive ? this.activeOperator : this.inactiveOperator,
        }

        let pagination = {
            page: this.page,
            itemsPerPage: this.itemsPerPage,
            sortProperty: this.sortProperty,
            sortDirection: this.sortDirection
        }

        this.organizationsService
            .getOperators(request, pagination)
            .subscribe({
                next: (response: HttpResponse<IOrganization[]>) => {
                    this.operators = response.body;
                    this.totalItems = this.totalElementsHeader.getTotalElements(response.headers);
                },
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    showActiveOperators() {
        this.page = 0;
        this.showActive = true;
        this.showInactive = false;
        this.filterUpdate();
    }

    showInactiveOperators() {
        this.page = 0;
        this.showActive = false;
        this.showInactive = true;
        this.filterUpdate();
    }

    goToDetailed(id) {
        this.router.navigate(['promoter/operators', id]).then();
    }

    setOperatorStatus(id, name, status) {
        const modal = this.modalService.open(OrganizationChangeStatusConfirmationDialogComponent);
        modal.componentInstance.name = name;
        modal.componentInstance.newStatus = status;
        modal.result.then(() => {
            this.organizationsService
                .setOperatorStatus(id, status)
                .subscribe({
                    next: () => this.fetchOperators(),
                    error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                })
        });
    }

    navigateToPage($event) {
        this.page = $event - 1;
        this.filterUpdate();
    }

    private filterUpdate() {
        this.router.navigate(['promoter/operators'], {
            queryParams: {
                name: this.operatorFilter == '' ? null : this.operatorFilter,
                active: this.showActive,
                page: this.page + 1
            }
        }).then()
    }

    resetFilter() {
        this.operatorFilter = '';
        this.filterUpdate();
    }

    trackId(index: number, item: IOrganization) {
        return item.id;
    }
}
