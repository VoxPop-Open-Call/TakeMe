import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { FamilityUserDriverSelectDialogComponent } from './famility-user-driver-select-dialog/famility-user-driver-select-dialog.component';

import { UserService } from 'app/shared/services/user.service';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { Account, UserBusType } from 'app/core';
import { OrganizationType } from 'app/shared/model/organization.model';
import { Driver } from 'app/shared/model/driver.model';

@Component({
    selector: 'jhi-famility-user-detail',
    templateUrl: './famility-user-detail.component.html',
    styles: []
})
export class FamilityUserDetailComponent implements OnInit, OnDestroy {
    email;
    account: Account;

    listProfilesBus = Object.keys(UserBusType);
    listProfiles = [];

    listSelectProfiles = [];

    showSelectDriver = false;
    driverSelected: Driver;

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private router: Router,
        private activatedRouter: ActivatedRoute,
        private modalService: NgbModal,
        private userService: UserService
    ) {}

    ngOnInit() {
        this.email = this.activatedRouter.snapshot.params['email'];

        this.loadUserInformation();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadUserInformation() {
        this.userService.getByEmail(this.email).subscribe(
            (result: HttpResponse<Account>) => {
                this.account = result.body;
                this.listProfiles = this.listProfilesBus;

                this.convertAuthorities();
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    convertAuthorities() {
        if (this.account.organization.organizationType === OrganizationType.BUS_COMPANY) {
            this.listProfiles.forEach((profile: string) => {
                const profileConverted = UserBusType[profile];

                this.account.authorities.forEach((authority: string) => {
                    if (authority === profileConverted) {
                        this.listSelectProfiles.push(profile);

                        if (profile === 'BUS_DRIVER') {
                            this.showSelectDriver = true;
                            this.driverSelected = this.account.driver;
                        }
                    }
                });
            });
        }
    }

    selectProfile(event, profile) {
        if (event.target.checked) {
            this.listSelectProfiles.push(profile);

            if (profile === 'BUS_DRIVER') {
                this.showSelectDriver = true;
            }
        } else {
            this.listSelectProfiles.splice(this.listSelectProfiles.indexOf(profile), 1);

            if (profile === 'BUS_DRIVER') {
                this.showSelectDriver = false;
                this.driverSelected = null;
            }
        }
    }

    isSelected(profile): boolean {
        return this.listSelectProfiles.indexOf(profile) > -1;
    }

    selectDriver() {
        const modal = this.modalService.open(FamilityUserDriverSelectDialogComponent);

        modal.componentInstance.driverSelected = this.driverSelected;
        modal.componentInstance.organizationId = this.account.organization.id;

        modal.result.then((driver: Driver) => {
            this.driverSelected = driver;
        });
    }

    update() {
        let request = {
            types: this.listSelectProfiles
        };

        if (this.driverSelected) {
            request = Object.assign({ driverId: this.driverSelected.id }, request);
        }

        this.userService.patchProfileUser(this.account.id, request).subscribe(
            () => this.goBack(),
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    goBack() {
        this.router.navigate(['promoter/users']);
    }
}
