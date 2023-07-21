import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { UsersDialogChangeStatusComponent } from './users-dialog-change-status.component';

import { UsersService } from './users.service';
import { Principal } from 'app/core';
import { TotalElementsHeader } from 'app/shared/util/total-elements-header';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { IUser } from 'app/core';
import {debounceTime, Subject} from "rxjs";
import {DEBOUNCE_TIME} from "../../../config/input.constants";

@Component({
    selector: 'jhi-organization-user',
    templateUrl: './users.component.html',
    providers: [UsersService]
})
export class UsersComponent implements OnInit, OnDestroy {
    firstNameFilter = '';
    lastNameFilter = '';
    page = 0;
    pageSize = 10;
    totalElements = 0;
    usersOrganization: IUser[] = [];

    textSearchUpdate = new Subject<string>();

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private modalService: NgbModal,
        private router: Router,
        private principal: Principal,
        private usersService: UsersService,
        private totalElementsHeader: TotalElementsHeader
    ) {
        this.textSearchUpdate.pipe(debounceTime(DEBOUNCE_TIME)).subscribe(() => this.loadUsersOrganization())
    }

    ngOnInit() {
        this.loadUsersOrganization();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadUsersOrganization() {
        this.usersService
            .getUsersOrganization(this.principal.getIdOrganization(), this.firstNameFilter, this.lastNameFilter, this.page, this.pageSize)
            .subscribe(
                (result: HttpResponse<IUser[]>) => {
                    this.usersOrganization = result.body;
                    this.totalElements = this.totalElementsHeader.getTotalElements(result.headers);
                },
                (error: HttpErrorResponse) => {
                    this.errorHandler.showError(error);
                }
            );
    }

    changeStateUser(userId: number, name: string, activated: boolean) {
        const modal = this.modalService.open(UsersDialogChangeStatusComponent);

        modal.componentInstance.name = name;
        modal.componentInstance.activated = activated;

        modal.result.then(() => {
            this.usersService.updateUserStatus(userId, activated).subscribe(
                () => {
                    this.loadUsersOrganization();
                },
                (error: HttpErrorResponse) => {
                    this.errorHandler.showError(error);
                }
            );
        });
    }

    changePage(page) {
        this.page = page - 1;
        this.loadUsersOrganization();
    }

    goBack() {
        this.router.navigate(['/famility']).then();
    }

    resetFilter() {
        this.firstNameFilter = '';
        this.loadUsersOrganization();
    }
}
