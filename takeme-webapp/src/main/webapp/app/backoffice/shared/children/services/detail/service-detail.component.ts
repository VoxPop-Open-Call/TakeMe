import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {ServicesService} from 'app/shared/services/services.service';
import {ServiceStopPointDaysOfWeekService} from 'app/shared/services/service-stop-point-days-of-week.service';
import {ErrorHandlerProviderService} from 'app/shared/providers/error-handler-provider.service';
import {IService} from 'app/shared/model/service.model';
import {IServiceStopPoint, StopPointType} from 'app/shared/model/service-stop-point.model';
import {Principal} from "../../../../../core";
import {Authority} from "../../../../../config/authority.constants";
import {Observable} from "rxjs";
import {TranslateService} from "@ngx-translate/core";

@Component({
    selector: 'jhi-bus-company-passengers-services-detail',
    templateUrl: './service-detail.component.html',
    providers: [ServiceStopPointDaysOfWeekService, ServicesService]
})
export class ServiceDetailComponent implements OnInit, OnDestroy {
    service: IService = null;
    collectionPoints: IServiceStopPoint[] = [];
    deliveryPoints: IServiceStopPoint[] = [];

    subscriptionId: number;
    serviceId: number;

    isOperator;

    constructor(
        private servicesService: ServicesService,
        private translateService: TranslateService,
        private errorHandler: ErrorHandlerProviderService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
    }

    ngOnInit() {
        this.principal
            .hasAuthority(Authority.OPERATOR)
            .then(isOperator => {
                this.isOperator = isOperator;

                this.activatedRoute.params.subscribe(params => {
                    this.subscriptionId = params['id'];
                    this.serviceId = params['serviceId'];

                    this.fetchService();
                });
            });
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    private fetchService() {
        this.servicesService
            .getService(this.serviceId)
            .subscribe({
                next: (response: HttpResponse<IService>) => {
                    this.service = response.body;

                    this.service.serviceStopPoints.forEach((servicePoint: IServiceStopPoint) => {
                        if (servicePoint.stopPointType === StopPointType.COLLECTION) {
                            this.collectionPoints.push(servicePoint);
                        } else {
                            this.deliveryPoints.push(servicePoint);
                        }
                    });
                },
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    previousState(): void {
        window.history.back();
    }

    goToUpdate() {
        this.router.navigate(['operator/passengers', this.subscriptionId, 'services', this.serviceId, 'edit']).then();
    }

    translateFrequency(frequency): Observable<string | any> {
        return frequency.id == 1 ? this.translateService.get('backoffice.home.busCompany.weekly') : this.translateService.get('backoffice.home.busCompany.fortnight');
    }
}
