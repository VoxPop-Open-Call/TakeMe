import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { OrganizationsService } from 'app/shared/services/organizations.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ChildStopPointsServices } from 'app/shared/model/child-stop-points-services.model';
import { BuildAddress } from 'app/shared/util/build-address';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';
import { ILocation } from 'app/shared/model/location.model';
import { Child } from 'app/shared/model/child.model';
import { ServiceStopPoint, StopPointType } from 'app/shared/model/service-stop-point.model';
import { ChildServices } from 'app/shared/model/child-services.model';
import {IService, Service} from 'app/shared/model/service.model';
import {PromoterItineraryService} from "../../../famility/promoter-itinerary/service/promoter-itinerary.service";

@Component({
    selector: 'jhi-stop-point-select-dialog',
    templateUrl: './stop-point-select-dialog.component.html',
    providers: [OrganizationsService]
})
export class StopPointSelectDialogComponent implements OnInit, OnDestroy {
    @Input()
    promoterItineraryId;

    @Input()
    selectedChildrenStopPointsService: ChildStopPointsServices[];

    childServicesList: ChildServices[] = [];

    stopPointTypes = StopPointType;

    constructor(
        private promoterItineraryService: PromoterItineraryService,
        private errorHandler: ErrorHandlerProviderService,
        private activeModal: NgbActiveModal
    ) {}

    ngOnInit() {
        this.fetchPromoterItineraryServiceStopPoints();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    dismiss() {
        this.activeModal.dismiss();
    }

    close() {
        this.activeModal.close(this.selectedChildrenStopPointsService);
    }

    buildAddress(location: ILocation): string {
        return BuildAddress.buildAddressFromLocationObject(location);
    }

    getPrivateLocationIconPath(isFamility: boolean): string {
        if (isFamility) {
            return '/content/icons/icon-TUTOR-yellow.svg';
        }
        return '/content/icons/icon-TUTOR.svg';
    }

    fetchPromoterItineraryServiceStopPoints() {
        this.promoterItineraryService.getPromoterItineraryServiceStopPoints(this.promoterItineraryId)
            .subscribe({
                next: (response: HttpResponse<ChildStopPointsServices[]>) => {
                    response.body.forEach(childrenStopPointsServices => {
                        const childServices = new ChildServices();
                        childServices.child = childrenStopPointsServices.child;
                        childServices.services = [];
                        childrenStopPointsServices.serviceStopPoints.forEach(serviceStopPoint => {
                            serviceStopPoint.match = this.isMatched(childServices.child, serviceStopPoint);
                            const serviceFilter = childServices.services.filter(service => service.id === serviceStopPoint.serviceId);
                            if (serviceFilter.length !== 0) {
                                const foundService = serviceFilter[0];
                                foundService.serviceStopPoints.push(serviceStopPoint);
                                if (serviceStopPoint.match) {
                                    foundService.selectable = false;
                                }
                            } else {
                                const newService = new Service();
                                newService.id = serviceStopPoint.serviceId;
                                newService.serviceStopPoints = [];
                                newService.selected = false;
                                newService.selectable = !serviceStopPoint.match;
                                newService.serviceStopPoints.push(serviceStopPoint);
                                childServices.services.push(newService);
                            }
                        });
                        this.childServicesList.push(childServices);
                    });

                    this.childServicesList.forEach(childService => this.sortServicesByCollectionTime(childService.services));
                },
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    isMatched(selectedChild: Child, selectedServiceStopPoint: ServiceStopPoint) {
        let result = false;
        this.selectedChildrenStopPointsService.forEach((childStopPointService: ChildStopPointsServices) => {
            if (childStopPointService.child.id === selectedChild.id) {
                childStopPointService.serviceStopPoints.forEach((serviceStopPoint: ServiceStopPoint) => {
                    if (
                        serviceStopPoint.combinedTime === selectedServiceStopPoint.combinedTime &&
                        serviceStopPoint.location.id === selectedServiceStopPoint.location.id &&
                        serviceStopPoint.stopPointType === selectedServiceStopPoint.stopPointType
                    ) {
                        result = true;
                    }
                });
            }
        });
        return result;
    }

    selectService(service: Service, event) {
        service.selected = !service.selected;
    }

    addServiceStopPoints() {
        const serviceStopPointsToAdd: ChildStopPointsServices[] = [];
        this.childServicesList.forEach(childServices => {
            const serviceStopPointsForChild = [];
            let includeChild = false;
            childServices.services.forEach(service => {
                if (service.selected) {
                    includeChild = true;
                    service.serviceStopPoints.forEach(serviceStopPoint => {
                        serviceStopPointsForChild.push(serviceStopPoint);
                    });
                }
            });
            if (includeChild) {
                const childStopPointsServices = new ChildStopPointsServices();
                childStopPointsServices.child = childServices.child;
                childStopPointsServices.serviceStopPoints = [];
                childStopPointsServices.serviceStopPoints = serviceStopPointsForChild;
                serviceStopPointsToAdd.push(childStopPointsServices);
            }
        });
        this.activeModal.close(serviceStopPointsToAdd);
    }

    servicesSelected(): boolean {
        let servicesSelected = false;
        this.childServicesList.forEach(childServices => {
            childServices.services.forEach(service => {
                if (service.selected) {
                    servicesSelected = true;
                }
            });
        });
        return servicesSelected;
    }

    private sortServicesByCollectionTime(services: IService[]) {
        services.sort((s1, s2) => {
            if (s1.serviceStopPoints[0].combinedTime < s2.serviceStopPoints[0].combinedTime) {
                return -1;
            }

            if (s1.serviceStopPoints[0].combinedTime > s2.serviceStopPoints[0].combinedTime) {
                return 1;
            }

            return 0;
        });
    }
}
