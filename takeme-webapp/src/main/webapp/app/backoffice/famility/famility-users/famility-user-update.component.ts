import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';

import { FamilityUserSelectOrganizationComponent } from './famility-user-select-organization/famility-user-select-organization.component';
import { FamilityUserDriverSelectDialogComponent } from './famility-user-driver-select-dialog/famility-user-driver-select-dialog.component';

import { UserService } from 'app/shared/services/user.service';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { Organization, OrganizationType } from 'app/shared/model/organization.model';
import { UserBusType } from 'app/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Driver } from 'app/shared/model/driver.model';

@Component({
    selector: 'jhi-famility-user-update',
    templateUrl: './famility-user-update.component.html',
    styles: ['div.ng-invalid { border: transparent }']
})
export class FamilityUserUpdateComponent implements OnInit, OnDestroy {
    userForm: FormGroup;

    showDriverSelect = false;

    organizationType = OrganizationType;
    listOrganizationTypes = Object.keys(OrganizationType);
    listProfilesBus = Object.keys(UserBusType);
    listProfiles = [];

    driverSelect: Driver = new Driver();
    organizationSelect: Organization = new Organization();
    listSelectProfiles: String[] = [];

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private modalService: NgbModal,
        private formBuild: FormBuilder,
        private router: Router,
        private userService: UserService
    ) {
    }

    ngOnInit() {
        this.userForm = this.formBuild.group({
            email: [undefined, Validators.required],
            password: [undefined, [Validators.minLength(6), Validators.required]],
            firstName: [undefined, Validators.required],
            lastName: [undefined, Validators.required],
            typeOrganization: [undefined, Validators.required],
            organization: this.formBuild.group({
                id: [undefined, Validators.required],
                name: [undefined, Validators.required]
            }),
            driver: this.formBuild.group({
                id: [undefined],
                name: [undefined]
            })
        });
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    selectDriver(driverForm: AbstractControl) {
        const modal = this.modalService.open(FamilityUserDriverSelectDialogComponent);

        modal.componentInstance.driverSelected = this.driverSelect;
        modal.componentInstance.organizationId = this.organizationSelect.id;

        modal.result.then((driver: Driver) => {
            this.driverSelect = driver;

            driverForm.patchValue({
                id: driver.id,
                name: driver.name
            });
        });
    }

    selectOrganization(organizationForm: AbstractControl) {
        const modal = this.modalService.open(FamilityUserSelectOrganizationComponent);

        modal.componentInstance.organizationType = this.userForm.value.typeOrganization;
        modal.componentInstance.organizationSelected = this.organizationSelect;

        modal.result.then(
            (organization: Organization) => {
                this.organizationSelect = organization;

                organizationForm.patchValue({
                    id: organization.id,
                    name: organization.name
                });

                organizationForm.updateValueAndValidity();
            },
            () => {}
        );
    }

    save() {
        const copyValues = Object.assign({}, this.userForm.value);

        copyValues.organizationId = copyValues.organization.id;
        copyValues.driverId = copyValues.driver.id;

        delete copyValues.organization;
        delete copyValues.typeOrganization;
        delete copyValues.driver;

        const request = {
            firebaseUser: copyValues,
            types: this.listSelectProfiles
        };

        this.userService.createWithMultipleRoles(request).subscribe(
            () => {
                this.goBack();
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    isSelected(profile): boolean {
        return this.listSelectProfiles.indexOf(profile) !== -1;
    }

    selectProfile(event, profile) {
        if (event.target.checked) {
            this.listSelectProfiles.push(profile);

            if (profile === 'BUS_DRIVER') {
                this.showDriverSelect = true;

                (<FormGroup>this.userForm.controls.driver).controls.id.setValidators(Validators.required);
                (<FormGroup>this.userForm.controls.driver).controls.name.setValidators(Validators.required);
            }
        } else {
            this.listSelectProfiles.splice(this.listSelectProfiles.indexOf(profile), 1);

            if (profile === 'BUS_DRIVER') {
                this.showDriverSelect = false;

                (<FormGroup>this.userForm.controls.driver).controls.id.clearValidators();
                (<FormGroup>this.userForm.controls.driver).controls.name.clearValidators();

                (<FormGroup>this.userForm.controls.driver).controls.id.patchValue(undefined);
                (<FormGroup>this.userForm.controls.driver).controls.name.patchValue(undefined);
            }
        }

        this.userForm.updateValueAndValidity();
    }

    cleanProfiles() {
        this.listSelectProfiles = [];
        this.showDriverSelect = false;
        this.listProfiles = this.listProfilesBus;
    }

    goBack() {
        this.router.navigate(['promoter/users']);
    }
}
