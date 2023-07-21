import {Component, OnInit, Input, OnDestroy} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder, Validators} from '@angular/forms';
import {HttpErrorResponse} from '@angular/common/http';
import {OrganizationsService} from 'app/shared/services/organizations.service';
import {Principal} from 'app/core';
import {LocationsService} from 'app/shared/services/locations.service';
import {ErrorHandlerProviderService} from 'app/shared/providers/error-handler-provider.service';
import {LocationType, ILocation} from 'app/shared/model/location.model';
import {TranslateService} from '@ngx-translate/core';
import {EMPTY, Observable} from 'rxjs';

@Component({
    selector: 'jhi-bus-company-passengers-services-update-update-location-dialog',
    templateUrl: './location-update-dialog.component.html',
    providers: [OrganizationsService, LocationsService]
})
export class LocationUpdateDialogComponent implements OnInit, OnDestroy {
    @Input()
    location: ILocation;

    locationTypes = Object.keys(LocationType);

    editForm;

    constructor(
        private organizationsService: OrganizationsService,
        private locationsService: LocationsService,
        private translateService: TranslateService,
        private errorHandler: ErrorHandlerProviderService,
        private principal: Principal,
        private formBuilder: FormBuilder,
        private activeModal: NgbActiveModal
    ) {
    }

    ngOnInit() {
        this.locationTypes.splice(this.locationTypes.indexOf(LocationType.SCHOOL), 1);

        this.editForm = this.formBuilder.group({
            id: [''],
            type: ['', Validators.required],
            designation: ['', Validators.required],
            street: ['', Validators.required],
            portNumber: ['', Validators.required],
            floor: [''],
            postalCode: ['', Validators.required],
            city: ['', Validators.required],
            country: ['', Validators.required],
            longitude: ['', Validators.required],
            latitude: ['', Validators.required],
            plusCode: ['']
        });

        if (this.location) {
            this.resetForm();
        }
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    private resetForm() {
        this.editForm.patchValue({
            id: this.location.id,
            type: this.location.type,
            designation: this.location.designation,
            street: this.location.street,
            portNumber: this.location.portNumber,
            floor: this.location.floor,
            postalCode: this.location.postalCode,
            city: this.location.city,
            country: this.location.country,
            longitude: this.location.longitude,
            latitude: this.location.latitude,
            plusCode: this.location.plusCode
        });
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

        return key != null ? this.translateService.get(key) : EMPTY;
    }

    dismiss() {
        this.activeModal.dismiss();
    }

    save() {
        if (this.location) {
            this.locationsService
                .update(this.editForm.value)
                .subscribe({
                    next: () => this.activeModal.close(),
                    error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                });
        } else {
            this.organizationsService
                .createLocation(this.principal.getIdOrganization(), this.editForm.value)
                .subscribe({
                    next: () => this.activeModal.close(),
                    error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                });
        }
    }

    delete() {
        this.organizationsService
            .deleteOrganizationServiceLocation(this.principal.getIdOrganization(), this.location.id)
            .subscribe({
                next: () => this.activeModal.close(),
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }
}
