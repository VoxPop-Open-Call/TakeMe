import {Component, OnDestroy, OnInit} from '@angular/core';
import {Principal} from 'app/core';
import {ChildrenService} from "../../../../shared/services/children.service";
import {IChild, StatusType} from "../../../../shared/model/child.model";
import {Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {SubscriptionDialogComponent} from "./subscription-dialog/subscription-dialog.component";
import {CommentDialogComponent} from "./comment-dialog/comment-dialog.component";
import {EnrollmentDialogComponent} from "./enrollment-dialog/enrollment-dialog.component";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {ErrorHandlerProviderService} from "../../../../shared/providers/error-handler-provider.service";
import {TotalElementsHeader} from "../../../../shared/util/total-elements-header";
import {IChildItinerarySubscription} from "../../../../entities/child-itinerary-subscription/child-itinerary-subscription.model";
import {IPhoto} from "../../../../shared/model/photo.model";

@Component({
    selector: 'jhi-tutor-passengers-list',
    templateUrl: './children.component.html'
})
export class ChildrenComponent implements OnInit, OnDestroy {
    children: IChild[] = [];

    activeSubscription = StatusType.ACTIVE;
    inactiveSubscription = StatusType.INACTIVE;
    pendingSubscription = StatusType.PENDING;

    totalItems = 0;
    page = 0;
    itemsPerPage = 10;
    sortProperty = 'name';
    sortDirection = 'asc';

    constructor(
        private childrenService: ChildrenService,
        private errorHandler: ErrorHandlerProviderService,
        private router: Router,
        private principal: Principal,
        private totalElementsHeader: TotalElementsHeader,
        private modalService: NgbModal
    ) {
    }

    ngOnInit() {
        this.fetchChildren();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    private fetchChildren() {
        let pagination = {
            page: this.page,
            itemsPerPage: this.itemsPerPage,
            sortProperty: this.sortProperty,
            sortDirection: this.sortDirection
        }

        this.childrenService
            .getChildren(this.principal.getUserIdentity().tutor.id, pagination)
            .subscribe({
                next: (response: HttpResponse<IChild[]>) => {
                    this.children = response.body;
                    this.totalItems = this.totalElementsHeader.getTotalElements(response.headers);
                    this.fetchChildItinerarySubscriptions();
                    this.fetchPhotos();
                },
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    private fetchChildItinerarySubscriptions() {
        this.children.forEach((child: IChild) => {
            let request = {
                childId: child.id
            }

            let pagination = {
                sortProperty: 'promoterItineraryName',
                sortDirection: 'asc'
            }

            this.childrenService
                .getChildItinerarySubscriptions(request, pagination)
                .subscribe({
                    next: (response: HttpResponse<IChildItinerarySubscription[]>) => child.itinerarySubscriptions = response.body,
                    error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                });
        });
    }

    private fetchPhotos() {
        this.children.forEach((child: IChild) => {
            if (child.photoId) {
                this.childrenService
                    .getChildPhoto(child.id, child.photoId)
                    .subscribe({
                        next: (response: HttpResponse<IPhoto>) => child.photo = response.body.photo,
                        error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                    });
            }
        });
    }

    goToEdit(id: number) {
        this.router.navigate(['tutor/passengers', id, 'edit']).then();
    }

    showSubscriptionModal(child) {
        const modal = this.modalService.open(SubscriptionDialogComponent, {size: 'lg'});
        modal.componentInstance.child = child;
        modal.result.then(enrollmentUrl => {
            this.fetchChildItinerarySubscriptions();
            this.showEnrollmentModal(enrollmentUrl);
        }, () => {
        });
    }

    showEnrollmentModal(enrollmentUrl) {
        const modal = this.modalService.open(EnrollmentDialogComponent);
        modal.componentInstance.enrollmentUrl = !enrollmentUrl.startsWith("https://") ? ("https://" + enrollmentUrl) : enrollmentUrl;
    }

    showCommentModal(comment) {
        const modal = this.modalService.open(CommentDialogComponent);
        modal.componentInstance.comment = comment;
    }

    navigateToPage($event: number) {
        this.page = $event - 1;
        this.fetchChildren();
    }
}
