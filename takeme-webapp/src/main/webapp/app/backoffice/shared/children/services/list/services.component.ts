import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {TotalElementsHeader} from 'app/shared/util/total-elements-header';
import {BuildAddress} from 'app/shared/util/build-address';
import {OrganizationsService} from 'app/shared/services/organizations.service';
import {Principal} from 'app/core';
import {ErrorHandlerProviderService} from 'app/shared/providers/error-handler-provider.service';
import {ILocation} from 'app/shared/model/location.model';
import {IService} from "../../../../../shared/model/service.model";
import {ChildrenService} from "../../../../../shared/services/children.service";
import {Authority} from "../../../../../config/authority.constants";

@Component({
    selector: 'jhi-bus-company-passengers-services-list',
    templateUrl: './services.component.html',
    providers: [OrganizationsService]
})
export class ServicesComponent implements OnInit, OnDestroy {
    services: IService[] = [];

    isOperator;

    subscriptionId;

    totalItems = 0;
    page = 0;
    itemsPerPage = 10;

    constructor(
        private childrenService: ChildrenService,
        private errorHandler: ErrorHandlerProviderService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private totalElementsHeader: TotalElementsHeader
    ) {
    }

    ngOnInit() {
        this.principal
            .hasAuthority(Authority.OPERATOR)
            .then(isOperator => {
                this.isOperator = isOperator;

                this.activatedRoute.params.subscribe(params => {
                    this.subscriptionId = params['id'];

                    this.fetchServices();
                });
            });
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    private fetchServices() {
        let pagination = {
            page: this.page,
            itemsPerPage: this.itemsPerPage
        }

        this.childrenService
            .getServicesBySubscriptionId(this.subscriptionId, pagination)
            .subscribe({
                next: (response: HttpResponse<IService[]>) => {
                    this.services = response.body;
                    this.totalItems = this.totalElementsHeader.getTotalElements(response.headers);
                    this.sortServicesByCollectionTime();
                },
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    previousState(): void {
        window.history.back();
    }

    goToCreate() {
        this.router.navigate(['operator/passengers', this.subscriptionId, 'services', 'new']).then();
    }

    goToDetailed(id) {
        if (!this.isOperator) {
            this.router.navigate(['promoter/passengers', this.subscriptionId, 'services', id]).then();
        } else {
            this.router.navigate(['operator/passengers', this.subscriptionId, 'services', id]).then();
        }
    }

    buildAddress(location: ILocation) {
        return BuildAddress.buildAddressFromLocationObject(location);
    }

    navigateToPage($event) {
        this.page = $event - 1;
        this.fetchServices();
    }

    private sortServicesByCollectionTime() {
        this.services.sort((s1, s2) => {
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
