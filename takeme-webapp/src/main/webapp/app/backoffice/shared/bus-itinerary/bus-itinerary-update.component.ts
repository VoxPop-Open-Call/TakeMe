import {Component, OnDestroy, OnInit} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { NgbDateParserFormatter, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import moment from 'moment';
import * as _ from 'lodash';

import { StopPointSelectDialogComponent } from './stop-point/stop-point-select-dialog.component';
import { LocationSelectDialogComponent } from './location/location-select-dialog.component';
import { VehicleDialogComponent } from './vehicle/vehicle-dialog/vehicle-dialog.component';
import { DriverDialogComponent } from './driver/driver-dialog/driver-dialog.component';
import { ErrorNoAllChildCompletePopupComponent } from './error-no-all-child-complete-popup/error-no-all-child-complete-popup.component';

import { CustomNgbDateParserFormatter } from 'app/shared/util/ngbDateParseFormatter';

import { ItineraryService } from 'app/shared/services/itinerary.service';
import { BuildAddress } from 'app/shared/util/build-address';
import { Principal } from 'app/core';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { Itinerary } from 'app/shared/model/itinerary.model';
import { Vehicle } from 'app/shared/model/vehicle.model';
import { Driver } from 'app/shared/model/driver.model';
import { ItineraryStopPoint } from 'app/shared/model/itinerary-stop-point.model';
import { ILocation, Location } from 'app/shared/model/location.model';
import { ChildStopPointsServices } from 'app/shared/model/child-stop-points-services.model';
import { ServiceStopPoint, StopPointType } from 'app/shared/model/service-stop-point.model';
import { Child } from 'app/shared/model/child.model';
import { BusItineraryDialogComponent } from './bus-itinerary-dialog/bus-itinerary-dialog.component';
import { Moment } from 'moment';
import { IPromoterItinerary } from "../../famility/promoter-itinerary/promoter-itinerary.model";
import { PromoterItineraryService } from "../../famility/promoter-itinerary/service/promoter-itinerary.service";
import { forkJoin, Observable } from "rxjs";
import { TranslateService } from "@ngx-translate/core";
import { ErrorDialogComponent } from "./error-dialog/error-dialog.component";

@Component({
    selector: 'jhi-bus-itinerary-update',
    templateUrl: './bus-itinerary-update.component.html',
    styles: ['div.ng-invalid { border: transparent }'],
    providers: [{ provide: NgbDateParserFormatter, useClass: CustomNgbDateParserFormatter }, ItineraryService]
})
export class BusItineraryUpdateComponent implements OnInit, OnDestroy {
    itineraryId;
    template = false;

    itineraryForm: FormGroup;

    stopPointType = StopPointType;

    selectedChildrenStopPointsService: ChildStopPointsServices[] = [];

    itinerary: Itinerary = new Itinerary();
    itineraryStopPointList: ItineraryStopPoint[] = [];

    promoterItineraries: IPromoterItinerary[] = [];

    isOptionSelected = false;

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private activatedRoute: ActivatedRoute,
        private modalService: NgbModal,
        private router: Router,
        private formBuilder: FormBuilder,
        private itineraryService: ItineraryService,
        private principal: Principal,
        private promoterItineraryService: PromoterItineraryService,
        private translateService: TranslateService
    ) {}

    ngOnInit() {
        this.itineraryForm = this.formBuilder.group({
            id: [],
            name: ['', Validators.required],
            scheduledTime: ['', Validators.required],
            selectedHour: ['', Validators.required],
            organization: this.formBuilder.group({
                id: [this.principal.getIdOrganization()]
            }),
            estimatedStartLocation: this.formBuilder.group({
                id: [undefined, Validators.required],
                designation: [undefined, Validators.required],
                street: [],
                portNumber: [],
                floor: [],
                postalCode: [],
                city: [],
                country: [],
                longitude: [],
                latitude: [],
                plusCode: []
            }),
            vehicle: this.formBuilder.group({
                id: ['', Validators.required],
                designation: ['', Validators.required],
                licensePlate: [],
                capacity: ['', Validators.required]
            }),
            driver: this.formBuilder.group({
                id: [undefined, Validators.required],
                name: [undefined, Validators.required],
                driverLicense: []
            }),
            itineraryStopPointList: this.formBuilder.array([]),
            promoterItinerary: [null, Validators.required],
        });

        this.itineraryId = this.activatedRoute.snapshot.params['id'];

        this.fetchPromoterItineraries();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadItinerary() {
        const url = this.activatedRoute.snapshot.url;

        if (url[url.length - 1].path === 'copy') {
            this.template = true;
        }

        this.itineraryService.get(this.itineraryId).subscribe(
            (result: HttpResponse<Itinerary>) => {
                this.loadFormWithData(result.body);
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    loadFormWithData(itinerary: Itinerary) {
        this.itinerary = itinerary;
        this.itineraryStopPointList = this.itinerary.itineraryStopPointList;

        if (this.template) {
            itinerary.id = undefined;
            itinerary.effectiveStartTime = undefined;
            itinerary.effectiveEndTime = undefined;
            itinerary.estimatedTotalTime = undefined;
            itinerary.estimatedKM = undefined;
            itinerary.itineraryStopPointList.forEach(itineraryStopPoint => {
                itineraryStopPoint.id = undefined;
                itineraryStopPoint.stopPoint.id = undefined;
                itineraryStopPoint.estimatedTime = undefined;
            });
        }

        this.itineraryForm.patchValue({
            id: itinerary.id,
            name: itinerary.name,
            scheduledTime: itinerary.scheduledTime,
            selectedHour: itinerary.scheduledTime.clone().format('HH:mm'),
            estimatedStartLocation: itinerary.estimatedStartLocation,
            organization: itinerary.organization,
            vehicle: itinerary.vehicle,
            driver: itinerary.driver,
            promoterItinerary: this.getPromoterItinerarySelection(itinerary.promoterItinerary)
        });

        this.buildListSelectedChildrenStopPointsService(itinerary);

        this.itineraryForm.updateValueAndValidity();
    }

    buildListSelectedChildrenStopPointsService(itinerary: Itinerary) {
        itinerary.itineraryStopPointList.forEach((itineraryStopPoint: ItineraryStopPoint) => {
            itineraryStopPoint.stopPoint.childList.forEach((child: Child) => {
                let childExist = false;

                this.selectedChildrenStopPointsService.forEach((childSelectedStopPoint: ChildStopPointsServices) => {
                    if (childSelectedStopPoint.child.id === child.id) {
                        childExist = true;

                        const serviceStopPoint: ServiceStopPoint = new ServiceStopPoint();
                        serviceStopPoint.location = itineraryStopPoint.stopPoint.location;
                        serviceStopPoint.combinedTime = itineraryStopPoint.stopPoint.scheduledTime.format('HH:mm');
                        serviceStopPoint.stopPointType = itineraryStopPoint.stopPoint.stopPointType;

                        childSelectedStopPoint.serviceStopPoints.push(serviceStopPoint);
                    }
                });

                if (!childExist) {
                    const childStopPointService = new ChildStopPointsServices();
                    childStopPointService.child = child;
                    childStopPointService.serviceStopPoints = [];

                    const serviceStopPoint: ServiceStopPoint = new ServiceStopPoint();
                    serviceStopPoint.location = itineraryStopPoint.stopPoint.location;
                    serviceStopPoint.combinedTime = itineraryStopPoint.stopPoint.scheduledTime.format('HH:mm');
                    serviceStopPoint.stopPointType = itineraryStopPoint.stopPoint.stopPointType;

                    childStopPointService.serviceStopPoints.push(serviceStopPoint);

                    this.selectedChildrenStopPointsService.push(childStopPointService);
                }
            });
        });
    }

    save() {
        if (!this.collectionsAndDeliveriesCheck()) {
            return;
        }
        const copy = _.cloneDeep(this.itineraryForm.value);
        const selectedDate: Moment = moment(copy.scheduledTime, 'DD-MM-YYYY');

        copy.itineraryStopPointList = this.itineraryStopPointList;

        if (!this.areAllChildrenComplete()) {
            const modal = this.modalService.open(ErrorNoAllChildCompletePopupComponent);
            modal.result.then(() => {}, () => {});
        } else {
            copy.itineraryStopPointList.forEach((itineraryStopPoint: ItineraryStopPoint) => {
                itineraryStopPoint.stopPoint.scheduledTime.clone().set({
                    year: selectedDate.year(),
                    month: selectedDate.month(),
                    day: selectedDate.day()
                });
            });

            copy.scheduledTime = copy.scheduledTime
                .hour(parseInt(copy.selectedHour.split(':')[0]))
                .minute(parseInt(copy.selectedHour.split(':')[1]));

            if (this.itineraryId && !this.template) {
                this.itineraryService.update(copy).subscribe(
                    () => {
                        this.goBack();
                    },
                    (error: HttpErrorResponse) => {
                        this.errorHandler.showError(error);
                    }
                );
            } else {
                this.itineraryService.create(copy).subscribe(
                    (result: HttpResponse<Itinerary>) => {
                        this.router.navigate(['operator/itineraries']);
                    },
                    (error: HttpErrorResponse) => {
                        this.errorHandler.showError(error);
                    }
                );
            }
        }
    }

    private collectionsAndDeliveriesCheck(): boolean {
        let auxChildArray: number[] = [];
        let breakMe = false;
        this.itineraryStopPointList.forEach(isp => {
            if (!breakMe) {
                let stopPointType = isp.stopPoint.stopPointType;
                isp.stopPoint.childList.forEach(child => {
                    if (!breakMe) {
                        let indexOfChild = auxChildArray.indexOf(child.id);
                        if (stopPointType === 'COLLECTION') {
                            auxChildArray.push(child.id);
                        } else {
                            if (indexOfChild > -1) {
                                auxChildArray.splice(indexOfChild, 1);
                            } else {
                                const collectionsAndDeliveriesCheckModal = this.modalService.open(ErrorDialogComponent);
                                collectionsAndDeliveriesCheckModal.componentInstance.message = child.name + this.translateService.instant('backoffice.home.busCompany.itinerary.must-be-collected-first');
                                collectionsAndDeliveriesCheckModal.result.then(() => {}, () => {});
                                breakMe = true;
                            }
                        }
                    }
                })
            }
        })
        return !breakMe;
    }

    addItineraryStopPoint() {
        const modal = this.modalService.open(StopPointSelectDialogComponent, { size: 'lg' });

        modal.componentInstance.promoterItineraryId = this.itineraryForm.get('promoterItinerary').value.id;
        modal.componentInstance.selectedChildrenStopPointsService = this.selectedChildrenStopPointsService;

        modal.result.then(
            (resultSelectedChildrenStopPointsService: ChildStopPointsServices[]) => {
                this.updateSelectedChildrenStopPointsServiceWithNewNewPoints(resultSelectedChildrenStopPointsService);

                resultSelectedChildrenStopPointsService.forEach((childStopPointService: ChildStopPointsServices) => {
                    childStopPointService.serviceStopPoints.forEach((serviceStopPoint: ServiceStopPoint) => {
                        const itineraryStopPointsList: ItineraryStopPoint[] = this.itineraryStopPointList;

                        let exist = false;
                        let countItineraryStopPoint = 0;

                        itineraryStopPointsList.forEach((itineraryStopPoint: ItineraryStopPoint) => {
                            if (
                                serviceStopPoint.location.id === itineraryStopPoint.stopPoint.location.id &&
                                serviceStopPoint.combinedTime === itineraryStopPoint.stopPoint.scheduledTime.format('HH:mm') &&
                                serviceStopPoint.stopPointType === itineraryStopPoint.stopPoint.stopPointType
                            ) {
                                exist = true;

                                const newChild: Child = {
                                    id: childStopPointService.child.id,
                                    name: childStopPointService.child.name
                                };

                                itineraryStopPointsList[countItineraryStopPoint].stopPoint.childList.push(newChild);
                            }

                            countItineraryStopPoint++;
                        });

                        if (!exist) {
                            // stop point does not exist

                            const itineraryStopPoint: ItineraryStopPoint = {
                                order: itineraryStopPointsList.length,
                                stopPoint: {
                                    stopPointType: serviceStopPoint.stopPointType,
                                    scheduledTime: moment(serviceStopPoint.combinedTime, 'HH:mm'),
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
                                        plusCode: serviceStopPoint.location.plusCode
                                    },
                                    childList: [
                                        {
                                            id: childStopPointService.child.id,
                                            name: childStopPointService.child.name
                                        }
                                    ]
                                }
                            };
                            itineraryStopPointsList.push(itineraryStopPoint);
                        }
                    });
                });
                this.sortItineraryStopPointList();
            },
            () => {}
        );
    }

    updateSelectedChildrenStopPointsServiceWithNewNewPoints(childStopPointsServicesToAdd: ChildStopPointsServices[]): void {
        childStopPointsServicesToAdd.forEach(childStopPointService => {
            const childServicesToUpdateFilter = this.selectedChildrenStopPointsService.filter(
                childStopPointsServices => childStopPointsServices.child.id === childStopPointService.child.id
            );
            if (childServicesToUpdateFilter.length !== 0) {
                const childServicesToUpdate = childServicesToUpdateFilter[0];
                childStopPointService.serviceStopPoints.forEach(serviceStopPoint => {
                    childServicesToUpdate.serviceStopPoints.push(serviceStopPoint);
                });
            } else {
                const newChildStopPointServices = new ChildStopPointsServices();
                newChildStopPointServices.child = childStopPointService.child;
                newChildStopPointServices.serviceStopPoints = [];
                newChildStopPointServices.serviceStopPoints = childStopPointService.serviceStopPoints;
                this.selectedChildrenStopPointsService.push(newChildStopPointServices);
            }
        });
    }

    selectEstimatedStartLocation(locationForm: AbstractControl) {
        const modal = this.modalService.open(LocationSelectDialogComponent, { size: 'lg' });

        modal.componentInstance.organizationId = this.principal.getIdOrganization();
        modal.componentInstance.locationSelected = locationForm.value;

        modal.result.then(
            (location: Location) => {
                locationForm.patchValue({
                    id: location.id,
                    designation: location.designation,
                    street: location.street,
                    portNumber: location.portNumber,
                    floor: location.floor,
                    postalCode: location.postalCode,
                    city: location.city,
                    country: location.country,
                    longitude: location.longitude,
                    latitude: location.latitude,
                    plusCode: location.plusCode
                });
            },
            () => {}
        );
    }

    selectDriver(driverForm: AbstractControl) {
        const modal = this.modalService.open(DriverDialogComponent);

        modal.componentInstance.organizationId = this.principal.getIdOrganization();
        modal.componentInstance.driverSelected = driverForm.value;

        modal.result.then(
            (driver: Driver) => {
                driverForm.patchValue({
                    id: driver.id,
                    name: driver.name,
                    driverLicense: driver.driverLicense
                });
            },
            () => {}
        );
    }

    selectVehicle(vehicleForm: AbstractControl) {
        const modal = this.modalService.open(VehicleDialogComponent);

        modal.componentInstance.organizationId = this.principal.getIdOrganization();
        modal.componentInstance.vehicleSelected = vehicleForm.value;

        modal.result.then(
            (vehicle: Vehicle) => {
                vehicleForm.patchValue({
                    id: vehicle.id,
                    designation: vehicle.designation,
                    licensePlate: vehicle.licensePlate,
                    capacity: vehicle.capacity
                });
            },
            () => {}
        );
    }

    changePositionStopPoint(event: CdkDragDrop<string[]>) {
        const tempStopPoint = this.itineraryStopPointList[event.previousIndex];

        this.itineraryStopPointList.splice(event.previousIndex, 1);

        this.itineraryStopPointList.splice(event.currentIndex, 0, tempStopPoint);

        this.itineraryStopPointList.forEach(itineraryStopPoint => {
            itineraryStopPoint.order = this.itineraryStopPointList.indexOf(itineraryStopPoint);
        });
    }

    deleteWrapper(indexItineraryStopPoint: number, indexChildList: number) {
        const itineraryStopPoint = this.itineraryStopPointList[indexItineraryStopPoint];
        const stopPoint = itineraryStopPoint.stopPoint;
        const childId = stopPoint.childList[indexChildList].id;
        const locationId = stopPoint.location.id;
        const stopPointType = stopPoint.stopPointType;
        const scheduledTime = stopPoint.scheduledTime;
        this.deleteChildFromStopPoint(itineraryStopPoint, childId, locationId, stopPointType, indexItineraryStopPoint, indexChildList);
        const pairCoordinates = this.findSuitablePair(childId, locationId, stopPointType, scheduledTime);
        const pairIndexItineraryStopPoint = pairCoordinates[0];
        const pairIndexChildList = pairCoordinates[1];
        if (pairIndexItineraryStopPoint !== -1 && pairIndexChildList !== -1) {
            const pairItineraryStopPoint = this.itineraryStopPointList[pairIndexItineraryStopPoint];
            const pairStopPointType = this.getPairStopPointType(stopPointType);
            const pairLocationId = this.getLocationId(pairIndexItineraryStopPoint);
            this.deleteChildFromStopPoint(
                pairItineraryStopPoint,
                childId,
                pairLocationId,
                pairStopPointType,
                pairIndexItineraryStopPoint,
                pairIndexChildList
            );
        }
    }

    getPairStopPointType(stopPointType: string): StopPointType {
        if (stopPointType === StopPointType.COLLECTION) {
            return StopPointType.DELIVER;
        }
        return StopPointType.COLLECTION;
    }

    getLocationId(indexItineraryStopPoint: number): number {
        return this.itineraryStopPointList[indexItineraryStopPoint].stopPoint.location.id;
    }

    deleteChildFromStopPoint(
        itineraryStopPoint: ItineraryStopPoint,
        childId,
        locationId,
        stopPointType,
        indexItineraryStopPoint,
        indexChildList
    ) {
        itineraryStopPoint.stopPoint.childList.splice(indexChildList, 1);
        if (itineraryStopPoint.stopPoint.childList.length === 0) {
            this.itineraryStopPointList.splice(indexItineraryStopPoint, 1);
            const orderValueRemoved = itineraryStopPoint.order;
            this.itineraryStopPointList.forEach((itStopPoint: ItineraryStopPoint) => {
                if (itStopPoint.order < orderValueRemoved) {
                    itStopPoint.order--;
                }
            });
        }
        this.updateSelectedChildrenStopPointsService(childId, locationId, stopPointType);
    }

    findSuitablePair(childId, locationId, stopPointType: StopPointType, scheduledTime): number[] {
        const itineraryStopPointList = this.itineraryStopPointList;
        let indexItineraryStopPoint = -1;
        let indexChildList = -1;
        loop1: for (let i = 0; i < itineraryStopPointList.length; i++) {
            const stopPoint = itineraryStopPointList[i].stopPoint;
            const childList = stopPoint.childList;
            for (let j = 0; j < childList.length; j++) {
                if (childId === childList[j].id && locationId !== stopPoint.location.id && stopPointType !== stopPoint.stopPointType) {
                    if (
                        (stopPointType === StopPointType.COLLECTION && scheduledTime < stopPoint.scheduledTime) ||
                        (stopPointType === StopPointType.DELIVER && scheduledTime > stopPoint.scheduledTime)
                    ) {
                        indexItineraryStopPoint = i;
                        indexChildList = j;
                        break loop1;
                    }
                }
            }
        }
        if (indexItineraryStopPoint === -1 || indexChildList === -1) {
            console.log('Could not find a suitable pair to the selected entry to remove.');
        }
        return [indexItineraryStopPoint, indexChildList];
    }

    updateSelectedChildrenStopPointsService(childId: number, locationId: number, stopPointType: StopPointType) {
        this.selectedChildrenStopPointsService.forEach((childStopPointsService: ChildStopPointsServices) => {
            if (childStopPointsService.child.id === childId) {
                childStopPointsService.serviceStopPoints.forEach((serviceStopPoint: ServiceStopPoint) => {
                    if (serviceStopPoint.location.id === locationId && serviceStopPoint.stopPointType === stopPointType) {
                        childStopPointsService.serviceStopPoints.splice(
                            childStopPointsService.serviceStopPoints.indexOf(serviceStopPoint),
                            1
                        );
                    }
                });

                if (childStopPointsService.serviceStopPoints.length === 0) {
                    this.selectedChildrenStopPointsService.splice(
                        this.selectedChildrenStopPointsService.indexOf(childStopPointsService),
                        1
                    );
                }
            }
        });
    }

    deleteItinerary() {
        const modal = this.modalService.open(BusItineraryDialogComponent);

        modal.componentInstance.actionType = 'delete';

        modal.result.then(
            () =>
                this.itineraryService.delete(this.itineraryId).subscribe(
                    () => {
                        this.router.navigate(['operator/itineraries']);
                    },
                    (error: HttpErrorResponse) => {
                        this.errorHandler.showError(error);
                    }
                ),
            () => {}
        );
    }

    goBack(): void {
        window.history.back();
    }

    buildAddress(location: ILocation): string {
        if (location.id) {
            return BuildAddress.buildAddressFromLocationObject(location);
        } else {
            return '';
        }
    }

    areAllChildrenComplete(): boolean {
        let result = true;

        this.selectedChildrenStopPointsService.forEach((childStopPointService: ChildStopPointsServices) => {
            let countCollectionDeliver = 0;

            childStopPointService.serviceStopPoints.forEach((serviceStopPoint: ServiceStopPoint) => {
                if (serviceStopPoint.stopPointType === StopPointType.COLLECTION) {
                    countCollectionDeliver++;
                } else if (serviceStopPoint.stopPointType === StopPointType.DELIVER) {
                    countCollectionDeliver--;
                }
            });

            if (countCollectionDeliver !== 0) {
                result = false;
            }
        });

        return result;
    }

    showStartingTimeWarning() {
        if (this.itineraryForm.get('scheduledTime').value !== '' && this.itineraryForm.get('selectedHour').value !== '') {
            const scheduledTimeInput = moment(this.itineraryForm.get('scheduledTime').value).set({
                hour: 0,
                minute: 0,
                second: 0,
                millisecond: 0
            });
            const selectedHourInput = this.itineraryForm.get('selectedHour').value;
            const hours = selectedHourInput.split(':')[0];
            const minutes = selectedHourInput.split(':')[1];
            scheduledTimeInput.add(hours, 'hours');
            scheduledTimeInput.add(minutes, 'minutes');
            const now = moment();
            return now.isAfter(scheduledTimeInput);
        }
        return false;
    }

    sortItineraryStopPointList() {
        const auxArray = this.itineraryStopPointList;
        auxArray.sort((s1, s2) => {
            if (s1.stopPoint.scheduledTime < s2.stopPoint.scheduledTime) {
                return -1;
            }
            if (s1.stopPoint.scheduledTime > s2.stopPoint.scheduledTime) {
                return 1;
            }
            return 0;
        });
        let counter = 0;
        auxArray.forEach(item => {
            item.order = counter;
            counter++;
        });
    }

    showReorderWarning(): boolean {
        return this.itineraryStopPointList.length > 0;
    }

    private fetchPromoterItineraries() {
      // the backend is using criteria that uses logical ANDs for the same field instead of ORs; so in order to keep
      // that paradigm, two requests are needed here
      const requests = [this.getPromoterItinerariesWithoutEndDate(), this.getPromoterItinerariesWithEndDateNotYetPassed()];
      forkJoin(requests)
        .subscribe({
          next: responses => {
            this.promoterItineraries = this.promoterItineraries.concat(responses[0].body);
            this.promoterItineraries = this.promoterItineraries.concat(responses[1].body);

              if (this.itineraryId) {
                  this.loadItinerary();
              }
          },
          error: error => {
            this.errorHandler.showError(error);
          }
        });
    }

    private getPromoterItinerariesWithoutEndDate(): Observable<HttpResponse<IPromoterItinerary[]>> {
      let params = {
        operatorId: this.principal.getIdOrganization(),
        active: true
      }
      return this.promoterItineraryService.getPromoterItineraries(params)
    }

    private getPromoterItinerariesWithEndDateNotYetPassed() : Observable<HttpResponse<IPromoterItinerary[]>> {
      let params = {
        operatorId: this.principal.getIdOrganization(),
        endDateHasNotPassed: true
      }
      return this.promoterItineraryService.getPromoterItineraries(params);
    }

    private getPromoterItinerarySelection(promoterItinerary: IPromoterItinerary) {
        let promoterItineraryFound: IPromoterItinerary = null;

        this.promoterItineraries.forEach((itinerary: IPromoterItinerary) => {
            if (itinerary.id == promoterItinerary.id) {
                promoterItineraryFound = itinerary;
            }
        });

        if (promoterItineraryFound) {
            this.isOptionSelected = true;
        }

        return promoterItineraryFound;
    }
}
