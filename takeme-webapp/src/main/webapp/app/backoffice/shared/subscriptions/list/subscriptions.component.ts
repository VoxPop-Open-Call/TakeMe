import {Component, OnDestroy, OnInit} from '@angular/core';
import {IChildItinerarySubscription} from "../../../../entities/child-itinerary-subscription/child-itinerary-subscription.model";
import {ErrorHandlerProviderService} from "../../../../shared/providers/error-handler-provider.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ActivatedRoute, Router} from "@angular/router";
import {Principal} from "../../../../core";
import {ChildrenService} from "../../../../shared/services/children.service";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {SubscriptionDialogComponent} from "./subscription-dialog/subscription-dialog.component";
import {CommentDialogComponent} from "../../../tutor/passengers/list/comment-dialog/comment-dialog.component";
import {StatusType} from "../../../../shared/model/child.model";
import {TotalElementsHeader} from "../../../../shared/util/total-elements-header";
import {IPhoto} from "../../../../shared/model/photo.model";
import {Authority} from "../../../../config/authority.constants";
import dayjs from "dayjs/esm";
import {IOrganization, OrganizationType} from "../../../../shared/model/organization.model";
import {OrganizationService} from "../../../../entities/organization/organization.service";
import {debounceTime, Subject} from "rxjs";
import {DEBOUNCE_TIME} from "../../../../config/input.constants";
import {PromoterServiceService} from "../../../famility/promoter-service/service/promoter-service.service";
import {IPromoterService} from "../../../famility/promoter-service/promoter-service.model";

@Component({
    selector: 'jhi-bus-company-subscriptions-list',
    templateUrl: './subscriptions.component.html'
})
export class SubscriptionsComponent implements OnInit, OnDestroy {
    pendingSubscriptions: IChildItinerarySubscription[] = [];
    activeSubscriptions: IChildItinerarySubscription[] = [];
    inactiveSubscriptions: IChildItinerarySubscription[] = [];

    activeSubscription = StatusType.ACTIVE;
    inactiveSubscription = StatusType.INACTIVE;
    pendingSubscription = StatusType.PENDING;

    isOperator;

    passengerFilter = '';
    promoterItineraryFilter = '';
    promoterServiceFilter = '';
    promoterServices: IPromoterService[] = [];
    operatorFilter = '';
    operators: IOrganization[] = [];

    pendingSortProperty = 'subscriptionDate';
    pendingSortDirection = 'desc';

    activeTotalItems = 0;
    activePage = 0;
    activeItemsPerPage = 5;
    activeSortProperty = 'activationDate';
    activeSortDirection = 'desc';

    inactiveTotalItems = 0;
    inactivePage = 0;
    inactiveItemsPerPage = 5;
    inactiveSortProperty = 'deactivationDate';
    inactiveSortDirection = 'desc';

    textSearchUpdate = new Subject<string>();

    constructor(
        private childrenService: ChildrenService,
        private errorHandler: ErrorHandlerProviderService,
        private router: Router,
        private principal: Principal,
        private totalElementsHeader: TotalElementsHeader,
        private modalService: NgbModal,
        private organizationService: OrganizationService,
        private promoterServiceService: PromoterServiceService,
        private activatedRoute: ActivatedRoute
    ) {
        this.textSearchUpdate.pipe(debounceTime(DEBOUNCE_TIME)).subscribe(() => this.filterUpdate())
    }

    ngOnInit() {
        this.principal
            .hasAuthority(Authority.OPERATOR)
            .then(isOperator => {
                this.isOperator = isOperator;

                this.activatedRoute.queryParams.subscribe(params => {
                    if (params.passenger) {
                        this.passengerFilter = params.passenger;
                    }

                    if (params.itinerary) {
                        this.promoterItineraryFilter = params.itinerary;
                    }

                    if (!this.isOperator && params.service) {
                        this.promoterServiceFilter = params.service;
                    }

                    if (!this.isOperator && params.operator) {
                        this.operatorFilter = params.operator;
                    }

                    if (params.activePage) {
                        this.activePage = params.activePage - 1;
                    }

                    if (params.inactivePage) {
                        this.inactivePage = params.inactivePage - 1;
                    }

                    if (!this.isOperator) {
                        this.fetchPromoterServices();
                        this.fetchOperators();
                    }

                    this.fetchSubscriptions(this.pendingSubscription);
                    this.fetchSubscriptions(this.activeSubscription);
                    this.fetchSubscriptions(this.inactiveSubscription);
                });
            });
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    private fetchSubscriptions(status) {
        let request = {
            statusType: status,
            childName: this.passengerFilter,
            promoterServiceId: this.promoterServiceFilter,
            operatorId: !this.isOperator ? this.operatorFilter : this.principal.getIdOrganization(),
            promoterItineraryName: this.promoterItineraryFilter
        }

        let pagination;

        if (status == this.pendingSubscription) {
            pagination = {
                sortProperty: this.pendingSortProperty,
                sortDirection: this.pendingSortDirection
            }
        } else if (status == this.activeSubscription) {
            pagination = {
                page: this.activePage,
                itemsPerPage: this.activeItemsPerPage,
                sortProperty: this.activeSortProperty,
                sortDirection: this.activeSortDirection
            }
        } else {
            pagination = {
                page: this.inactivePage,
                itemsPerPage: this.inactiveItemsPerPage,
                sortProperty: this.inactiveSortProperty,
                sortDirection: this.inactiveSortDirection
            }
        }

        this.childrenService
            .getChildItinerarySubscriptions(request, pagination)
            .subscribe({
                next: (response: HttpResponse<IChildItinerarySubscription[]>) => {
                    if (status == this.pendingSubscription) {
                        this.pendingSubscriptions = response.body;
                    } else if (status == this.activeSubscription) {
                        this.activeSubscriptions = response.body;
                        this.activeTotalItems = this.totalElementsHeader.getTotalElements(response.headers);
                    } else {
                        this.inactiveSubscriptions = response.body;
                        this.inactiveTotalItems = this.totalElementsHeader.getTotalElements(response.headers);
                    }
                    this.fetchPhotos(status);
                },
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    private fetchPhotos(status) {
        if (status == this.pendingSubscription) {
            this.pendingSubscriptions.forEach((subscription: IChildItinerarySubscription) => {
                if (subscription.child.photoId) {
                    this.childrenService
                        .getChildPhoto(subscription.child.id, subscription.child.photoId)
                        .subscribe({
                            next: (response: HttpResponse<IPhoto>) => subscription.child.photo = response.body.photo,
                            error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                        });
                }
            });
        } else if (status == this.activeSubscription) {
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
        } else {
            this.inactiveSubscriptions.forEach((subscription: IChildItinerarySubscription) => {
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

    goToDetailed(id) {
        if (this.isOperator) {
            this.router.navigate(['operator/subscriptions', id]).then();
        } else {
            this.router.navigate(['promoter/subscriptions', id]).then();
        }
    }

    setSubscriptionStatus(subscription, status) {
        const modal = this.modalService.open(SubscriptionDialogComponent);
        modal.componentInstance.status = status;
        modal.result.then(result => {
                const currDayJS = dayjs();

                let body = {
                    statusType: status,
                    comments: result.comments,
                    additionalInformation: result.additionalInformation,
                    activationDate: currDayJS,
                    deactivationDate: status == this.activeSubscription ? subscription.deactivationDate : currDayJS
                }

                this.childrenService
                    .patchChildItinerarySubscription(subscription.id, subscription, body)
                    .subscribe({
                        next: () => {
                            this.fetchSubscriptions(this.pendingSubscription);
                            this.fetchSubscriptions(status == this.activeSubscription ? this.activeSubscription : this.inactiveSubscription);
                        },
                        error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                    })
            },
            () => {
            }
        );
    }

    showCommentModal(comment) {
        const modal = this.modalService.open(CommentDialogComponent);
        modal.componentInstance.comment = comment;
    }

    activeNavigateToPage($event) {
        this.activePage = $event - 1;
        this.filterUpdate();
    }

    inactiveNavigateToPage($event) {
        this.inactivePage = $event - 1;
        this.filterUpdate();
    }

    private filterUpdate() {
        if (!this.isOperator) {
            this.router.navigate(['promoter/subscriptions'], {
                queryParams: {
                    passenger: this.passengerFilter == '' ? null : this.passengerFilter,
                    itinerary: this.promoterItineraryFilter == '' ? null : this.promoterItineraryFilter,
                    service: this.promoterServiceFilter == '' ? null : this.promoterServiceFilter,
                    operator: this.operatorFilter == '' ? null : this.operatorFilter,
                    activePage: this.activePage + 1,
                    inactivePage: this.inactivePage + 1
                }
            }).then();
        } else {
            this.router.navigate(['operator/subscriptions'], {
                queryParams: {
                    passenger: this.passengerFilter == '' ? null : this.passengerFilter,
                    itinerary: this.promoterItineraryFilter == '' ? null : this.promoterItineraryFilter,
                    activePage: this.activePage + 1,
                    inactivePage: this.inactivePage + 1
                }
            }).then();
        }
    }

    resetFilter() {
        this.passengerFilter = '';
        this.promoterItineraryFilter = '';

        if (!this.isOperator) {
            this.promoterServiceFilter = '';
            this.operatorFilter = '';
        }

        this.filterUpdate();
    }
}
