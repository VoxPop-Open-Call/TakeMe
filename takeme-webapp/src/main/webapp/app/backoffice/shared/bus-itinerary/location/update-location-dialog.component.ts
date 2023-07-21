import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

import { OrganizationsService } from 'app/shared/services/organizations.service';
import { Principal } from 'app/core';
import { LocationsService } from 'app/shared/services/locations.service';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { LocationType, Location } from 'app/shared/model/location.model';
import { TranslateService } from '@ngx-translate/core';
import { EMPTY, Observable } from 'rxjs';

@Component({
    selector: 'jhi-update-location-dialog',
    templateUrl: './update-location-dialog.component.html',
    styles: [],
    providers: [OrganizationsService, LocationsService]
})
export class UpdateLocationDialogComponent implements OnInit, OnDestroy {
    @Input()
    location: Location;

    locationForm;
    locationTypes = Object.keys(LocationType);

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private activeModal: NgbActiveModal,
        private formBuilder: FormBuilder,
        private principal: Principal,
        private organizationsService: OrganizationsService,
        private locationsService: LocationsService,
        private translateService: TranslateService
    ) {}

    ngOnInit() {
        this.locationTypes.splice(this.locationTypes.indexOf(LocationType.SCHOOL), 1);

        this.locationForm = this.formBuilder.group({
            id: [''],
            designation: ['', Validators.required],
            street: ['', Validators.required],
            portNumber: ['', Validators.required],
            floor: [''],
            postalCode: ['', Validators.required],
            city: ['', Validators.required],
            country: ['', Validators.required],
            type: ['', Validators.required],
            longitude: ['', Validators.required],
            latitude: ['', Validators.required],
            plusCode: ['']
        });

        if (this.location) {
            this.loadDataForm();
        }
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadDataForm() {
        this.locationForm.patchValue({
            id: this.location.id,
            designation: this.location.designation,
            street: this.location.street,
            portNumber: this.location.portNumber,
            floor: this.location.floor,
            postalCode: this.location.postalCode,
            city: this.location.city,
            country: this.location.country,
            type: this.location.type,
            longitude: this.location.longitude,
            latitude: this.location.latitude,
            plusCode: this.location.plusCode
        });
    }

    dismiss() {
        this.activeModal.dismiss();
    }

    save() {
        if (this.location) {
            this.locationsService.update(this.locationForm.value).subscribe(
                () => {
                    this.activeModal.close();
                },
                (error: HttpErrorResponse) => {
                    this.errorHandler.showError(error);
                }
            );
        } else {
            this.organizationsService.createLocation(this.principal.getIdOrganization(), this.locationForm.value).subscribe(
                () => {
                    this.activeModal.close();
                },
                (error: HttpErrorResponse) => {
                    this.errorHandler.showError(error);
                }
            );
        }
    }

    delete() {
        this.organizationsService.deleteOrganizationServiceLocation(this.principal.getIdOrganization(), this.location.id).subscribe(
            () => {
                this.activeModal.close('delete');
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    getLocationTypeLabel(locationType: string): Observable<string | any> {
        let key;
        switch (locationType) {
            case 'PRIVATE':
                key = 'backoffice.home.busCompany.services.private';
                break;
            case 'SCHOOL_EXTERNAL':
                key = 'backoffice.home.busCompany.services.school-external';
                break;
            case 'SCHOOL':
                key = 'backoffice.home.busCompany.services.school';
                break;
            default:
                key = null;
                break;
        }
        if (key != null) {
            return this.translateService.get(key);
        } else {
            return EMPTY;
        }
    }
}
