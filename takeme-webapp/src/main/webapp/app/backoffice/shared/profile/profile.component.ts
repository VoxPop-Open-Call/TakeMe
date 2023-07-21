import { Component, OnInit, OnDestroy } from '@angular/core';
import { Principal } from 'app/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ProfileDialogNewPhotoComponent } from './profile-dialog-new-photo.component';
import { BuildAddress } from 'app/shared/util/build-address';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { Organization } from 'app/shared/model/organization.model';
import { ILocation } from 'app/shared/model/location.model';
import { OrganizationService } from "../../../entities/organization/organization.service";

@Component({
    selector: 'jhi-organization-profile',
    templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit, OnDestroy {
    organization: Organization;
    photo = '';

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private principal: Principal,
        private organizationService: OrganizationService,
        private ngModal: NgbModal
    ) {}

    ngOnInit() {
        this.loadOrganization();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadOrganization() {
        this.organizationService.find(+this.principal.getIdOrganization()).subscribe(
            (result: HttpResponse<Organization>) => {
                this.organization = result.body;
                this.loadPhotoOrganization();
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    loadPhotoOrganization() {
        if (this.organization.photoId) {
            this.organizationService.getPhoto(this.organization.id, this.organization.photoId).subscribe(
                (result: HttpResponse<any>) => {
                    this.photo = 'data:image/jpg;base64,' + result.body.photo;
                },
                (error: HttpErrorResponse) => {
                    this.errorHandler.showError(error);
                }
            );
        }
    }

    selectNewPhoto() {
        const modal = this.ngModal.open(ProfileDialogNewPhotoComponent);

        modal.componentInstance.idOrganization = +this.principal.getIdOrganization();

        modal.result.then(() => {
            this.loadOrganization();
        });
    }

    buildAddress(location: ILocation) {
        return BuildAddress.buildAddressFromLocationObject(location);
    }
}
