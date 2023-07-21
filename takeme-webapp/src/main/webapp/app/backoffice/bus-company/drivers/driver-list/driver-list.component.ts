import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse, HttpParams, HttpResponse } from '@angular/common/http';

import { Principal } from 'app/core';
import { PhotoService } from 'app/shared/photo/photo.service';
import { TotalElementsHeader } from 'app/shared/util/total-elements-header';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { IDriver } from 'app/shared/model/driver.model';
import { OrganizationsService } from 'app/shared/services/organizations.service';
import { debounceTime, Subject } from "rxjs";
import { DEBOUNCE_TIME } from "../../../../config/input.constants";

@Component({
    selector: 'jhi-driver-list',
    templateUrl: './driver-list.component.html',
    providers: [OrganizationsService],
    styles: []
})
export class DriverListComponent implements OnInit, OnDestroy {
    totalElements = 0;
    page = 0;
    pageSize = 10;
    nameFilter = '';
    driverList: IDriver[] = [];

    textSearchUpdate = new Subject<string>();

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private router: Router,
        private principal: Principal,
        private organizationService: OrganizationsService,
        private totalElementsHeader: TotalElementsHeader,
        private photoService: PhotoService
    ) {
        this.textSearchUpdate.pipe(debounceTime(DEBOUNCE_TIME)).subscribe(() => this.loadDrivers())
    }

    ngOnInit() {
        this.loadDrivers();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadDrivers() {
        const params: HttpParams = new HttpParams().set('name', this.nameFilter);
        this.organizationService.getDrivers(+this.principal.getIdOrganization(), this.page, this.pageSize, params).subscribe(
            (result: HttpResponse<IDriver[]>) => {
                this.driverList = result.body;
                this.totalElements = this.totalElementsHeader.getTotalElements(result.headers);
                this.loadDriversPhoto();
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    loadDriversPhoto() {
        this.driverList
            .filter(driver => {
                return driver.photoId != null;
            })
            .forEach(driver => {
                if (driver.photoId !== null) {
                    this.photoService.getPhotoDriver(driver.id, driver.photoId).subscribe(response => {
                        driver.photo = 'data:image/jpg;base64,' + response.body.photo;
                    });
                }
            });
    }

    changePage(newPage: number) {
        this.page = newPage - 1;
        this.loadDrivers();
    }

    goToEdit(id: number) {
        this.router.navigate(['operator/monitors', id, 'edit']).then();
    }

    resetFilter() {
        this.nameFilter = '';
        this.loadDrivers();
    }
}
