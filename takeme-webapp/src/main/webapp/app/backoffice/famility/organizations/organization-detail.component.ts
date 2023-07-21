import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { OrganizationsService } from 'app/shared/services/organizations.service';
import { VehiclesListDialogComponent } from './bus-company/vehicles-list-dialog.component';
import { DriversListDialogComponent } from './bus-company/drivers-list-dialog.component';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { IOrganization, OrganizationType } from 'app/shared/model/organization.model';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-organization-detail',
    templateUrl: './organization-detail.component.html',
    styles: [],
    providers: [OrganizationsService]
})
export class OrganizationDetailComponent implements OnInit, OnDestroy {
    idOrganization;

    typeOrganization;

    organization: IOrganization;
    organizationType = OrganizationType;

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private modalService: NgbModal,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private organizationsService: OrganizationsService
    ) {}

    ngOnInit() {
        this.typeOrganization = this.activatedRoute.snapshot.data.organizationType;

        this.activatedRoute.params.subscribe(params => {
            this.idOrganization = params['id'];
            this.organizationsService.getOrganizationDetail(this.idOrganization).subscribe(
                (res: HttpResponse<IOrganization>) => {
                    this.organization = res.body;
                },
                (error: HttpErrorResponse) => {
                    this.errorHandler.showError(error);
                }
            );
        });
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    goToEdit() {
        this.router.navigate(['promoter/operators', this.idOrganization, 'edit']);
    }

    goBack() {
        this.router.navigate(['promoter/operators']);
    }

    showVehicles() {
        const modal = this.modalService.open(VehiclesListDialogComponent);

        modal.componentInstance.idOrganization = this.idOrganization;

        modal.result.then(() => {}, () => {});
    }

    showDrivers() {
        const modal = this.modalService.open(DriversListDialogComponent);

        modal.componentInstance.idOrganization = this.idOrganization;

        modal.result.then(() => {}, () => {});
    }
}
