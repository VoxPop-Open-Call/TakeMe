import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {Principal} from 'app/core';
import {TotalElementsHeader} from 'app/shared/util/total-elements-header';
import {ErrorHandlerProviderService} from 'app/shared/providers/error-handler-provider.service';
import {IChildItinerarySubscription} from "../../../../entities/child-itinerary-subscription/child-itinerary-subscription.model";
import {ChildrenService} from "../../../../shared/services/children.service";
import {StatusType} from "../../../../shared/model/child.model";
import {IPhoto} from "../../../../shared/model/photo.model";
import {Authority} from "../../../../config/authority.constants";
import {debounceTime, Subject} from "rxjs";
import {DEBOUNCE_TIME} from "../../../../config/input.constants";
import {IOrganization, OrganizationType} from "../../../../shared/model/organization.model";
import {OrganizationService} from "../../../../entities/organization/organization.service";
import {PromoterServiceService} from "../../../famility/promoter-service/service/promoter-service.service";
import {IPromoterService} from "../../../famility/promoter-service/promoter-service.model";
import {IPromoterItinerary} from "../../../famility/promoter-itinerary/promoter-itinerary.model";
import {PromoterItineraryService} from "../../../famility/promoter-itinerary/service/promoter-itinerary.service";

@Component({
    selector: 'jhi-bus-company-passengers-list',
    templateUrl: './children.component.html'
})
export class ChildrenComponent implements OnInit, OnDestroy {
    activeSubscriptions: IChildItinerarySubscription[] = [];

    isOperator;

    nameFilter = '';
    promoterServiceFilter = '';
    promoterServices: IPromoterService[] = [];
    operatorFilter = '';
    operators: IOrganization[] = [];
    promoterItineraryFilter = '';
    promoterItineraries: IPromoterItinerary[] = [];

    totalItems = 0;
    page = 0;
    itemsPerPage = 10;
    sortProperty = 'activationDate';
    sortDirection = 'desc';

    textSearchUpdate = new Subject<string>();

    constructor(
        private promoterServiceService: PromoterServiceService,
        private organizationService: OrganizationService,
        private promoterItineraryService: PromoterItineraryService,
        private childrenService: ChildrenService,
        private errorHandler: ErrorHandlerProviderService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private totalElementsHeader: TotalElementsHeader
    ) {
        this.textSearchUpdate.pipe(debounceTime(DEBOUNCE_TIME)).subscribe(() => this.filterUpdate());
    }

    ngOnInit() {
        this.principal
            .hasAuthority(Authority.OPERATOR)
            .then(isOperator => {
                this.isOperator = isOperator;

                this.activatedRoute.queryParams.subscribe(params => {
                    if (params.name) {
                        this.nameFilter = params.name;
                    }

                    if (!this.isOperator && params.service) {
                        this.promoterServiceFilter = params.service;
                    }

                    if (!this.isOperator && params.operator) {
                        this.operatorFilter = params.operator;
                    }

                    if (!this.isOperator && params.itinerary) {
                        this.promoterItineraryFilter = params.itinerary;
                    }

                    if (params.page) {
                        this.page = params.page - 1;
                    }

                    if (!this.isOperator) {
                        this.fetchPromoterServices();
                        this.fetchOperators();
                        this.fetchPromoterItineraries();
                    }

                    this.fetchActiveSubscriptions();
                });
            });
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    private filterUpdate() {
        if (!this.isOperator) {
            this.router.navigate(['promoter/passengers'], {
                queryParams: {
                    name: this.nameFilter == '' ? null : this.nameFilter,
                    service: this.promoterServiceFilter == '' ? null : this.promoterServiceFilter,
                    operator: this.operatorFilter == '' ? null : this.operatorFilter,
                    itinerary: this.promoterItineraryFilter == '' ? null : this.promoterItineraryFilter,
                    page: this.page + 1
                }
            }).then();
        } else {
            this.router.navigate(['operator/passengers'], {
                queryParams: {
                    name: this.nameFilter == '' ? null : this.nameFilter,
                    page: this.page + 1
                }
            }).then();
        }
    }

    private fetchPromoterServices() {
        this.promoterServiceService
            .query()
            .subscribe({
                next: (response: HttpResponse<IPromoterService[]>) => this.promoterServices = response.body,
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    private fetchOperators() {
        let request = {
            type: OrganizationType.BUS_COMPANY
        }

        this.organizationService
            .query(request)
            .subscribe({
                next: (response: HttpResponse<IOrganization[]>) => this.operators = response.body,
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    private fetchPromoterItineraries() {
        this.promoterItineraryService
            .query()
            .subscribe({
                next: (response: HttpResponse<IPromoterItinerary[]>) => this.promoterItineraries = response.body,
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    private fetchActiveSubscriptions() {
        let request = {
            statusType: StatusType.ACTIVE,
            childName: this.nameFilter,
            promoterServiceId: this.promoterServiceFilter,
            operatorId: !this.isOperator ? this.operatorFilter : this.principal.getIdOrganization(),
            promoterItineraryId: this.promoterItineraryFilter
        }

        let pagination = {
            page: this.page,
            itemsPerPage: this.itemsPerPage,
            sortProperty: this.sortProperty,
            sortDirection: this.sortDirection
        }

        this.childrenService
            .getChildItinerarySubscriptions(request, pagination)
            .subscribe({
                next: (response: HttpResponse<IChildItinerarySubscription[]>) => {
                    this.activeSubscriptions = response.body;
                    this.totalItems = this.totalElementsHeader.getTotalElements(response.headers);
                    this.fetchPhotos();
                },
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    private fetchPhotos() {
        this.activeSubscriptions.forEach((subscription: IChildItinerarySubscription) => {
            if (subscription.child.photoId) {
                this.childrenService
                    .getChildPhoto(subscription.child.id, subscription.child.photoId)
                    .subscribe({
                        next: (response: HttpResponse<IPhoto>) => subscription.child.photo = response.body.photo,
                        error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                    });
            }
        });
    }

    resetFilter() {
        this.nameFilter = '';

        if (!this.isOperator) {
            this.promoterServiceFilter = '';
            this.operatorFilter = '';
            this.promoterItineraryFilter = '';
        }

        this.filterUpdate();
    }

    goToDetailed(id) {
        if (!this.isOperator) {
            this.router.navigate(['promoter/passengers', id]).then();
        } else {
            this.router.navigate(['operator/passengers', id]).then();
        }
    }

    navigateToPage($event) {
        this.page = $event - 1;
        this.filterUpdate();
    }
}
