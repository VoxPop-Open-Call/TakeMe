import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {LocationSelectDialogComponent} from './location-select-dialog/location-select-dialog.component';
import {ServiceStopPointFrequencyService} from 'app/shared/services/service-stop-point-frequency.service';
import {ServiceStopPointDaysOfWeekService} from 'app/shared/services/service-stop-point-days-of-week.service';
import {Principal} from 'app/core';
import {ServicesService} from 'app/shared/services/services.service';
import {ErrorHandlerProviderService} from 'app/shared/providers/error-handler-provider.service';
import {IServiceStopPointFrequency} from 'app/shared/model/service-stop-point-frequency.model';
import {IServiceStopPointDaysOfWeek} from 'app/shared/model/service-stop-point-days-of-week.model';
import {ILocation} from 'app/shared/model/location.model';
import {IServiceStopPoint, StatusType, StopPointType} from 'app/shared/model/service-stop-point.model';
import {IService} from 'app/shared/model/service.model';
import {ServiceDeleteDialog} from './service-delete-dialog/service-delete-dialog.component';
import {forkJoin, Observable} from 'rxjs';
import {TranslateService} from "@ngx-translate/core";

@Component({
    selector: 'jhi-bus-company-passengers-services-update',
    templateUrl: './service-update.component.html',
    styles: ['div.ng-invalid { border: transparent }'],
    providers: [ServiceStopPointFrequencyService, ServiceStopPointDaysOfWeekService, ServicesService]
})
export class ServiceUpdateComponent implements OnInit, OnDestroy {
    subscriptionId: number;
    serviceId: number;

    frequencyEnum: IServiceStopPointFrequency[] = [];
    weekdaysEnum: IServiceStopPointDaysOfWeek[] = [];
    service: IService = null;

    editForm: FormGroup;

    constructor(
        private serviceStopPointFrequencyService: ServiceStopPointFrequencyService,
        private serviceStopPointDaysOfWeekService: ServiceStopPointDaysOfWeekService,
        private servicesService: ServicesService,
        private translateService: TranslateService,
        private errorHandler: ErrorHandlerProviderService,
        private router: Router,
        private activatedRouter: ActivatedRoute,
        private principal: Principal,
        private formBuilder: FormBuilder,
        private modalService: NgbModal
    ) {
    }

    ngOnInit() {
        this.subscriptionId = this.activatedRouter.snapshot.params['id'];
        this.serviceId = this.activatedRouter.snapshot.params['serviceId'];

        this.editForm = new FormGroup({
            childItinerarySubscription: new FormGroup({id: new FormControl(this.subscriptionId)}),
            organization: new FormGroup({id: new FormControl(this.principal.getIdOrganization())}),
            startDate: new FormControl(null, {validators: [Validators.required, this.startDateValidator]}),
            endDate: new FormControl(null, {validators: [Validators.required, this.endDateValidator]}),
            recurrent: new FormControl(false, Validators.required),
            collectionPoints: new FormArray([]),
            deliveryPoints: new FormArray([]),
            serviceStopPoints: new FormArray([])
        }, this.stopPointValidator());

        this.fetchFormOptions();

        if (!this.serviceId) {
            this.addCollectionPoint();
            this.addDeliveryPoint();
        }
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    private startDateValidator(control: AbstractControl) {
        const startDate = control.value;
        const endDate = control.parent?.get('endDate')?.value;

        if (startDate && endDate && startDate > endDate) {
            return {'startDateAfterEndDate': true};
        }

        return null;
    }

    private endDateValidator(control: AbstractControl) {
        const startDate = control.parent?.get('startDate')?.value;
        const endDate = control.value;

        if (startDate && endDate && startDate > endDate) {
            return {'endDateBeforeStartDate': true};
        }

        return null;
    }

    // TODO: RETHINK THIS VALIDATION FOR MULTIPLE STOP POINTS
    private stopPointValidator(): ValidatorFn {
        return (control: AbstractControl) => {
            if (control && control instanceof FormGroup) {
                const pointsGroup = control as FormGroup;

                if (pointsGroup) {
                    const collectionPointArray = pointsGroup.get('collectionPoints') as FormArray;
                    const deliveryPointArray = pointsGroup.get('deliveryPoints') as FormArray;

                    if (collectionPointArray && deliveryPointArray) {
                        if (
                            collectionPointArray.length < 1 ||
                            deliveryPointArray.length < 1
                        ) {
                            return {stopPointLengthValidation: 'failed'};
                        }

                        if (
                            collectionPointArray.at(0).value.combinedTime &&
                            deliveryPointArray.at(0).value.combinedTime &&
                            collectionPointArray.at(0).value.combinedTime >= deliveryPointArray.at(0).value.combinedTime
                        ) {
                            return {stopPointScheduleValidation: 'failed'};
                        }

                        if (
                            collectionPointArray.at(0).value.serviceStopPointDaysOfWeeks.length < 1 ||
                            deliveryPointArray.at(0).value.serviceStopPointDaysOfWeeks.length < 1
                        ) {
                            return {stopPointWeekdaysValidation: 'failed'};
                        }

                        return null;
                    }
                }
            }

            return {stopPointLengthValidation: 'failed'};
        };
    }

    private fetchFormOptions() {
        forkJoin([this.fetchFrequency(), this.fetchWeekdays()]).subscribe(() => this.fetchService());
    }

    private fetchFrequency() {
        return new Observable(observer => {
            this.serviceStopPointFrequencyService
                .getAll()
                .subscribe({
                    next: (response: HttpResponse<IServiceStopPointFrequency[]>) => {
                        this.frequencyEnum = response.body;
                        observer.next();
                        observer.complete();
                    },
                    error: (error: HttpErrorResponse) => {
                        observer.error();
                        this.errorHandler.showError(error);
                    }
                });
        });
    }

    private fetchWeekdays() {
        return new Observable(observer => {
            this.serviceStopPointDaysOfWeekService
                .getAll()
                .subscribe({
                    next: (response: HttpResponse<IServiceStopPointDaysOfWeek[]>) => {
                        this.weekdaysEnum = response.body;
                        observer.next();
                        observer.complete();
                    },
                    error: (error: HttpErrorResponse) => {
                        observer.error();
                        this.errorHandler.showError(error);
                    }
                });
        });
    }

    private fetchService() {
        if (this.serviceId) {
            this.servicesService
                .getService(this.serviceId)
                .subscribe({
                    next: (response: HttpResponse<IService>) => {
                        this.service = response.body;
                        this.resetForm();
                    },
                    error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                });
        }
    }

    private createStopPointFormGroup(stopPointType): FormGroup {
        return this.formBuilder.group({
            combinedTime: ['', Validators.required],
            frequency: ['', Validators.required],
            startFrequencyDate: ['', Validators.required],
            serviceStopPointDaysOfWeeks: [[], Validators.minLength(1)],
            location: this.formBuilder.group({
                id: ['', Validators.required],
                designation: [{value: '', disabled: true}],
                street: [{value: '', disabled: true}],
                portNumber: [{value: '', disabled: true}],
                floor: [{value: '', disabled: true}],
                postalCode: [{value: '', disabled: true}],
                city: [{value: '', disabled: true}],
                country: [{value: '', disabled: true}],
                longitude: [{value: '', disabled: true}],
                latitude: [{value: '', disabled: true}],
                plusCode: [{value: '', disabled: true}],
                type: [{value: '', disabled: true}]
            }),
            stopPointType: [stopPointType],
            statusType: [StatusType.ACTIVE],
            startHour: ['NOT_USED', Validators.required]
        });
    }

    private resetForm() {
        this.editForm.patchValue({
            childItinerarySubscription: {id: this.subscriptionId},
            organization: {id: this.service.organization.id},
            startDate: this.service.startDate,
            endDate: this.service.endDate,
            recurrent: this.service.recurrent
        });

        if (this.service.recurrent) {
            this.editForm.controls.endDate.clearValidators();
            this.editForm.controls.endDate.updateValueAndValidity();
        }

        this.service.serviceStopPoints.forEach((serviceStopPoint: IServiceStopPoint) => {
            const point = this.createStopPointFormGroup(serviceStopPoint.stopPointType);

            point.patchValue({
                combinedTime: serviceStopPoint.combinedTime,
                startFrequencyDate: serviceStopPoint.startFrequencyDate,
                statusType: serviceStopPoint.statusType,
                frequency: this.frequencyEnum[this.getFrequencyIndex(serviceStopPoint)],
                location: {
                    id: serviceStopPoint.location.id,
                    designation: serviceStopPoint.location.designation,
                    street: serviceStopPoint.location.street,
                    portNumber: serviceStopPoint.location.portNumber,
                    floor: serviceStopPoint.location.floor,
                    postalCode: serviceStopPoint.location.postalCode,
                    city: serviceStopPoint.location.city,
                    country: serviceStopPoint.location.country,
                    longitude: serviceStopPoint.location.longitude,
                    latitude: serviceStopPoint.location.latitude,
                    plusCode: serviceStopPoint.location.plusCode,
                    type: serviceStopPoint.location.type
                },
                serviceStopPointDaysOfWeeks: serviceStopPoint.serviceStopPointDaysOfWeeks
            });

            if (serviceStopPoint.stopPointType === StopPointType.COLLECTION) {
                (this.editForm.get('collectionPoints') as FormArray).push(point);
            } else {
                (this.editForm.get('deliveryPoints') as FormArray).push(point);
            }
        });
    }

    addCollectionPoint() {
        (this.editForm.get('collectionPoints') as FormArray).push(this.createStopPointFormGroup(StopPointType.COLLECTION));
    }

    removeCollectionPoint(index) {
        (this.editForm.get('collectionPoints') as FormArray).removeAt(index);
    }

    addDeliveryPoint() {
        (this.editForm.get('deliveryPoints') as FormArray).push(this.createStopPointFormGroup(StopPointType.DELIVER));
    }

    removeDeliveryPoint(index) {
        (this.editForm.get('deliveryPoints') as FormArray).removeAt(index);
    }

    save() {
        const service = Object.assign({}, this.editForm.value);

        const serviceStopPoints = [];
        service.collectionPoints.forEach(servicePoint => serviceStopPoints.push(servicePoint));
        service.deliveryPoints.forEach(servicePoint => serviceStopPoints.push(servicePoint));
        service.serviceStopPoints = serviceStopPoints;

        delete service.collectionPoints;
        delete service.deliveryPoints;

        if (this.serviceId) {
            service.id = this.serviceId;
            this.servicesService
                .updateService(service)
                .subscribe({
                    next: () => this.previousState(),
                    error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                });
        } else {
            this.servicesService
                .createService(service)
                .subscribe({
                    next: () => this.previousState(),
                    error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                });
        }
    }

    delete() {
        const modal = this.modalService.open(ServiceDeleteDialog);
        modal.result.then(
            () => this.servicesService
                .deleteService(this.serviceId)
                .subscribe({
                    next: () => this.router.navigate(['operator/passengers', this.subscriptionId, 'services']).then(),
                    error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                }),
            () => {
            }
        );
    }

    previousState(): void {
        window.history.back();
    }

    isSelected(weekday, servicePoint: any) {
        let result = false;

        servicePoint.value.serviceStopPointDaysOfWeeks.forEach(serviceWeekday => {
            if (weekday.id === serviceWeekday.id) result = true;
        });

        return result;
    }

    selectEndless($event) {
        if ($event.currentTarget.checked) {
            this.editForm.controls.endDate.disable();
            this.editForm.controls.endDate.patchValue('');
            this.editForm.controls.endDate.clearValidators();
            this.editForm.controls.endDate.updateValueAndValidity();
        } else {
            this.editForm.controls.endDate.enable();
            this.editForm.controls.endDate.setValidators([Validators.required, this.endDateValidator]);
            this.editForm.controls.endDate.updateValueAndValidity();
            this.editForm.controls.startDate.updateValueAndValidity();
        }
    }

    private getFrequencyIndex(serviceStopPoint) {
        let index = 0, count = 0;

        this.frequencyEnum.forEach((frequencyStop: IServiceStopPointFrequency) => {
            if (serviceStopPoint.frequency.id === frequencyStop.id) index = count;
            count++;
        });

        return index;
    }

    selectWeekday(event, weekday, servicePoint) {
        const serviceWeekdays = servicePoint.get('serviceStopPointDaysOfWeeks').value;

        if (event.currentTarget.checked) {
            serviceWeekdays.push(weekday);
        } else {
            let index = 0, count = 0;
            serviceWeekdays.forEach(serviceWeekday => {
                if (serviceWeekday.id === weekday.id) index = count;
                count++;
            });
            serviceWeekdays.splice(index, 1);
        }

        servicePoint.get('serviceStopPointDaysOfWeeks').updateValueAndValidity();
    }

    selectAddress(locationForm) {
        const modal = this.modalService.open(LocationSelectDialogComponent, {size: 'xl'});
        modal.componentInstance.organizationId = this.principal.getIdOrganization();
        modal.result.then(
            (location: ILocation) => {
                locationForm.id.patchValue(location.id);
                locationForm.designation.patchValue(location.designation);
                locationForm.street.patchValue(location.street);
                locationForm.portNumber.patchValue(location.portNumber);
                locationForm.floor.patchValue(location.floor);
                locationForm.postalCode.patchValue(location.postalCode);
                locationForm.city.patchValue(location.city);
                locationForm.country.patchValue(location.country);
                locationForm.longitude.patchValue(location.longitude);
                locationForm.latitude.patchValue(location.latitude);
                locationForm.plusCode.patchValue(location.plusCode);
                locationForm.type.patchValue(location.type);
            },
            () => {
            }
        );
    }

    translateFrequency(frequency): Observable<string | any> {
        return frequency.id == 1 ? this.translateService.get('backoffice.home.busCompany.weekly') : this.translateService.get('backoffice.home.busCompany.fortnight');
    }
}
