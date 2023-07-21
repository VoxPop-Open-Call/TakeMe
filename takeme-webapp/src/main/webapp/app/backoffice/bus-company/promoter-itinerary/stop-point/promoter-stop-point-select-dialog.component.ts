import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { OrganizationsService } from 'app/shared/services/organizations.service';
import { HttpResponse } from '@angular/common/http';
import { BuildAddress } from 'app/shared/util/build-address';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { ILocation } from 'app/entities/location/location.model';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { PromoterStopPointFormGroup, PromoterStopPointFormService } from "../../../famility/promoter-stop-point/update/promoter-stop-point-form.service";
import dayjs from "dayjs";
import { IPromoterItinerary } from "../promoter-itinerary.model";
import { map } from "rxjs/operators";
import { IPromoterStopPoint } from "../../../famility/promoter-stop-point/promoter-stop-point.model";
import { OrganizationService } from "../../../../entities/organization/organization.service";
import { Principal } from "../../../../core";
import { LocationUpdateDialogComponent } from "../../../shared/children/services/update/location-update-dialog/location-update-dialog.component";

@Component({
    selector: 'jhi-stop-point-select-dialog',
    templateUrl: './promoter-stop-point-select-dialog.component.html',
    providers: [OrganizationsService]
})
export class PromoterStopPointSelectDialogComponent implements OnInit, OnDestroy {
    @Input()
    promoterItinerary: IPromoterItinerary;

    promoterItineraryStopPoint: IPromoterStopPoint;

    locations: ILocation[] = [];
    selectedLocation: ILocation;
    stopPointForm: FormGroup;
    finalStopPointForm: PromoterStopPointFormGroup = this.promoterStopPointFormService.createPromoterStopPointFormGroup();

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private activeModal: NgbActiveModal,
        private organizationService: OrganizationService,
        private formBuilder: FormBuilder,
        protected promoterStopPointFormService: PromoterStopPointFormService,
        protected principal: Principal,
        protected modalService: NgbModal,
    ) {}

    ngOnInit() {
        this.stopPointForm = this.formBuilder.group(
            {
                name: [null, Validators.required],
                time: [null, Validators.required],
            }
        );

        this.prepareLocationsList();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    dismiss() {
        this.activeModal.dismiss();
    }

    close() {
        this.activeModal.close(this.promoterItineraryStopPoint);
    }

    buildAddress(location: ILocation): string {
        return BuildAddress.buildAddressFromLocationObject(location);
    }

    prepareLocationsList() {
      this.organizationService
        .findLocationsByOrganization(this.principal.getIdOrganization())
        .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
        .subscribe((locations: ILocation[]) => (this.locations = locations));
    }

    createPromoterStopPoint() {
      this.finalStopPointForm.controls.name.patchValue(this.stopPointForm.controls.name.value);
      this.finalStopPointForm.controls.scheduledTime.patchValue(
        dayjs()
          .set('date', 1)
          .set('month', 0)
          .set('year', 1970)
          .set('hour', this.stopPointForm.controls.time.value.split(':')[0])
          .set('minute', this.stopPointForm.controls.time.value.split(':')[1])
          .format('YYYY-MM-DDTHH:mm')
      );
      this.finalStopPointForm.controls.promoterItineraryId.patchValue(this.promoterItinerary.id);
      this.finalStopPointForm.controls.location.patchValue(this.selectedLocation);

      this.promoterItineraryStopPoint = this.promoterStopPointFormService.getPromoterStopPoint(this.finalStopPointForm);
      this.close();
    }

    selectLocation(location: ILocation) {
        this.selectedLocation = location;
    }

    createAddress() {
        const modal = this.modalService.open(LocationUpdateDialogComponent);
        modal.result.then(
            () => this.prepareLocationsList(),
            () => {
            }
        );
    }
}
