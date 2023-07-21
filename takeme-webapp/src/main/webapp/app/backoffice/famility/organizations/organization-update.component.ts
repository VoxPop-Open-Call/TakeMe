import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { OrganizationsService } from 'app/shared/services/organizations.service';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { IOrganization, Organization, OrganizationType, StatusType } from 'app/shared/model/organization.model';
import { Location, LocationType } from 'app/shared/model/location.model';
import { Contact } from 'app/shared/model/contact.model';

@Component({
    selector: 'jhi-organization-update',
    templateUrl: './organization-update.component.html',
    styles: [],
    providers: [OrganizationsService]
})
export class OrganizationUpdateComponent implements OnInit, OnDestroy {
    typeOrganization;
    idOrganization;

    organization: IOrganization;
    contContacts = 0;

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private organizationsService: OrganizationsService
    ) {}

    ngOnInit() {
        this.typeOrganization = this.activatedRoute.snapshot.data.organizationType;

        this.idOrganization = this.activatedRoute.snapshot.params['id'];

        if (this.idOrganization) {
            this.organizationsService.getOrganizationDetail(this.idOrganization).subscribe(
                (result: HttpResponse<IOrganization>) => {
                    this.organization = result.body;
                },
                (error: HttpErrorResponse) => {
                    this.errorHandler.showError(error);
                }
            );
        } else {
            this.organization = new Organization();
            this.organization.statusType = StatusType.INACTIVE;
            this.organization.location = new Location();
            this.organization.contacts = [];
            this.organization.location.type = LocationType.PRIVATE;
            this.organization.organizationType = OrganizationType.BUS_COMPANY;
        }
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    save() {
        if (this.organization.id === undefined) {
            this.organizationsService.createOrganization(this.organization).subscribe(
                () => {
                    this.goBack();
                },
                (error: HttpErrorResponse) => {
                    this.errorHandler.showError(error);
                }
            );
        } else {
            this.organizationsService.updateOrganization(this.organization).subscribe(
                () => {
                    this.goBack();
                },
                (error: HttpErrorResponse) => {
                    this.errorHandler.showError(error);
                }
            );
        }
    }

    trackContactFunction() {
        this.contContacts++;
        return this.contContacts;
    }

    addNewContact() {
        this.organization.contacts.push(new Contact());
    }

    deleteContact(index: number) {
        this.organization.contacts.splice(index, 1);
    }

    goBack() {
        window.history.back();
    }
}
