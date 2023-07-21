import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse, HttpParams, HttpResponse } from '@angular/common/http';

import { Principal } from 'app/core';
import { OrganizationsService } from 'app/shared/services/organizations.service';
import { TotalElementsHeader } from 'app/shared/util/total-elements-header';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { IVehicle } from 'app/shared/model/vehicle.model';
import { debounceTime, Subject } from "rxjs";
import { DEBOUNCE_TIME } from "../../../../config/input.constants";

@Component({
    selector: 'jhi-vehicle-list',
    templateUrl: './vehicle-list.component.html',
    providers: [OrganizationsService],
    styles: []
})
export class VehicleListComponent implements OnInit, OnDestroy {
    totalElements = 0;
    page = 0;
    pageSize = 10;

    designationFilter = '';

    vehicleList: IVehicle[] = [];

    textSearchUpdate = new Subject<string>();

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private router: Router,
        private principal: Principal,
        private organizationService: OrganizationsService,
        private totalElementsHeader: TotalElementsHeader
    ) {
        this.textSearchUpdate.pipe(debounceTime(DEBOUNCE_TIME)).subscribe(() => this.loadVehicles())
    }

    ngOnInit() {
        this.loadVehicles();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadVehicles() {
        const params: HttpParams = new HttpParams().set('designation', this.designationFilter);
        this.organizationService.getVehicles(+this.principal.getIdOrganization(), this.page, this.pageSize, params).subscribe(
            (result: HttpResponse<IVehicle[]>) => {
                this.vehicleList = result.body;
                this.totalElements = this.totalElementsHeader.getTotalElements(result.headers);
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    changePage(newPage: number) {
        this.page = newPage - 1;
        this.loadVehicles();
    }

    goToEdit(id: number) {
        this.router.navigate(['operator/vehicles', id, 'edit']).then();
    }

    resetFilter() {
        this.designationFilter = '';
        this.loadVehicles();
    }
}
