import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {ActivatedRoute, Router} from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import { UserService } from 'app/shared/services/user.service';
import { TotalElementsHeader } from 'app/shared/util/total-elements-header';

import { FamilityUserChangeStatusDialogComponent } from './famility-user-change-status-dialog/famility-user-change-status-dialog.component';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { Account } from 'app/core';
import {OrganizationType} from "../../../shared/model/organization.model";
import {debounceTime, Subject} from "rxjs";
import {DEBOUNCE_TIME} from "../../../config/input.constants";

@Component({
    selector: 'jhi-famility-users',
    templateUrl: './famility-users.component.html',
    styles: []
})
export class FamilityUsersComponent implements OnInit, OnDestroy {
    organizationType = OrganizationType;
    active = true;
    inactive = false;

    firstNameFilter = '';
    lastNameFilter = '';

    page = 0;
    pageSize = 20;
    totalElements = 0;
    listUsers: Account[] = [];

    textSearchUpdate = new Subject<string>();

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private translateService: TranslateService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private modalService: NgbModal,
        private userService: UserService,
        private totalElementsHeader: TotalElementsHeader
    ) {
        this.textSearchUpdate.pipe(debounceTime(DEBOUNCE_TIME)).subscribe(() => this.filterUpdate())
    }

    ngOnInit() {
        this.activatedRoute.queryParams.subscribe(params => {
            if (params.firstName) {
                this.firstNameFilter = params.firstName;
            }

            if (params.lastName) {
                this.lastNameFilter = params.lastName;
            }

            if (params.active) {
                this.active = params.active == 'true';
            }

            if (params.page) {
                this.page = params.page - 1;
            }

            this.loadListUsers();
        });
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadListUsers() {
        this.userService.getAll('', this.firstNameFilter, this.lastNameFilter, this.active, this.page, this.pageSize).subscribe(
            (result: HttpResponse<Account[]>) => {
                this.listUsers = result.body;
                this.totalElements = this.totalElementsHeader.getTotalElements(result.headers);
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    updateStatusUser(userId, status: boolean) {
        const modal = this.modalService.open(FamilityUserChangeStatusDialogComponent);

        modal.componentInstance.activate = status;

        modal.result.then(
            () => {
                this.userService.patchStatus(userId, status).subscribe(() => {
                    this.loadListUsers();
                });
            },
            () => {}
        );
    }

    showActiveUsers() {
        this.page = 0;
        this.active = true;
        this.filterUpdate();
    }

    showInactiveUsers() {
        this.page = 0;
        this.active = false;
        this.filterUpdate();
    }

    changePage(newPage) {
        this.page = newPage - 1;
        this.filterUpdate();
    }

    buildAuthorities(authorities: String[]): String {
        const authoritiesTransformed: String[] = [];

        if (authorities.indexOf('ROLE_USER') > -1) {
            authorities.splice(authorities.indexOf('ROLE_USER'), 1);
        }

        authorities.forEach((authority: string) => {
            if (authority !== 'ROLE_TUTOR' && authority !== 'ROLE_FAMILITY') {
                authoritiesTransformed.push(this.translateService.instant('familityBackofficeApp.RoleType.' + authority));
            } else {
                authority = 'null';
            }
        });

        return authoritiesTransformed.join(', ');
    }

    goAdd() {
        this.router.navigate(['promoter/users/new']);
    }

    goDetail(email) {
        this.router.navigate(['promoter/users', email]);
    }

    private filterUpdate() {
        this.router.navigate(['promoter/users'], {
            queryParams: {
                firstName: this.firstNameFilter == '' ? null : this.firstNameFilter,
                lastName: this.lastNameFilter == '' ? null : this.lastNameFilter,
                active: this.active,
                page: this.page + 1
            }
        }).then()
    }

    resetFilter() {
        this.firstNameFilter = '';
        this.lastNameFilter = '';
        this.filterUpdate();
    }
}
