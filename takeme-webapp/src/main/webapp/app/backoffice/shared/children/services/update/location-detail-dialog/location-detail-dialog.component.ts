import {Component, OnInit, Input, OnDestroy} from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {LocationUpdateDialogComponent} from '../location-update-dialog/location-update-dialog.component';
import {LocationsService} from 'app/shared/services/locations.service';
import {ErrorHandlerProviderService} from 'app/shared/providers/error-handler-provider.service';
import {ILocation} from 'app/shared/model/location.model';
import {EMPTY, Observable} from "rxjs";
import {TranslateService} from "@ngx-translate/core";

@Component({
    selector: 'jhi-bus-company-passengers-services-update-detail-location-dialog',
    templateUrl: './location-detail-dialog.component.html',
    providers: [LocationsService]
})
export class LocationDetailDialogComponent implements OnInit, OnDestroy {
    @Input()
    locationId;

    location: ILocation;

    constructor(
        private locationsService: LocationsService,
        private translateService: TranslateService,
        private errorHandler: ErrorHandlerProviderService,
        private modalService: NgbModal,
        private activeModal: NgbActiveModal
    ) {
    }

    ngOnInit() {
        this.fetchLocation();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    private fetchLocation() {
        this.locationsService
            .get(this.locationId)
            .subscribe({
                next: (response: HttpResponse<ILocation>) => this.location = response.body,
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    getLocationType(locationType: string): Observable<string | any> {
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

    update() {
        const modal = this.modalService.open(LocationUpdateDialogComponent);
        modal.componentInstance.location = this.location;
        modal.result.then(
            () => this.activeModal.close(),
            () => {
            }
        );
    }
}
