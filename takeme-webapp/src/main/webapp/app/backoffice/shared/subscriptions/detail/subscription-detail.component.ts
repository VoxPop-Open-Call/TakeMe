import { Component, OnDestroy, OnInit } from "@angular/core";
import { ErrorHandlerProviderService } from "../../../../shared/providers/error-handler-provider.service";
import { ActivatedRoute } from "@angular/router";
import { PhotoService } from "../../../../shared/photo/photo.service";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { HttpErrorResponse, HttpResponse } from "@angular/common/http";
import { SubscriptionDialogComponent } from "../list/subscription-dialog/subscription-dialog.component";
import { AdditionalInformationDialogComponent } from "./additional-information-dialog/additional-information-dialog.component";
import { IChildItinerarySubscription } from "../../../../entities/child-itinerary-subscription/child-itinerary-subscription.model";
import { ChildrenService } from "../../../../shared/services/children.service";
import { ITutor } from "../../../../shared/model/tutor.model";
import { StatusType } from "../../../../shared/model/child.model";
import { IPhoto } from "../../../../shared/model/photo.model";
import { CommentDialogComponent } from "../list/comment-dialog/comment-dialog.component";
import dayjs from "dayjs/esm";
import { Authority } from "../../../../config/authority.constants";
import { Principal } from "../../../../core";

@Component({
    selector: 'jhi-bus-company-subscriptions-detail',
    templateUrl: './subscription-detail.component.html'
})
export class SubscriptionDetailComponent implements OnInit, OnDestroy {
    subscription: IChildItinerarySubscription = null;
    tutors: ITutor[] = [];

    activeSubscription = StatusType.ACTIVE;
    inactiveSubscription = StatusType.INACTIVE;
    pendingSubscription = StatusType.PENDING;

    isOperator = true;

    constructor(
        private childrenService: ChildrenService,
        private photoService: PhotoService,
        private errorHandler: ErrorHandlerProviderService,
        private activatedRoute: ActivatedRoute,
        private modalService: NgbModal,
        private principal: Principal
    ) {
    }

    ngOnInit() {
        this.activatedRoute.params.subscribe(params => this.fetchSubscription(params['id']));
        this.principal.hasAuthority(Authority.OPERATOR).then(result => {
            this.isOperator = result;
        });
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    private fetchSubscription(id) {
        this.childrenService
            .getChildItinerarySubscription(id)
            .subscribe({
                next: (response: HttpResponse<IChildItinerarySubscription>) => {
                    this.subscription = response.body;
                    this.fetchTutors();
                    this.fetchPhotos(true);
                },
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    private fetchTutors() {
        this.childrenService
            .getChildTutors(this.subscription.child.id)
            .subscribe({
                next: (response: HttpResponse<ITutor[]>) => {
                    this.tutors = response.body;
                    this.fetchPhotos(false);
                },
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    private fetchPhotos(isFetchChildPhoto: boolean) {
        if (isFetchChildPhoto) {
            if (this.subscription.child.photoId) {
                this.childrenService
                    .getChildPhoto(this.subscription.child.id, this.subscription.child.photoId)
                    .subscribe({
                        next: (response: HttpResponse<IPhoto>) => this.subscription.child.photo = response.body.photo,
                        error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                    });
            }
        } else {
            this.tutors.forEach((tutor: ITutor) => {
                if (tutor.photoId) {
                    this.photoService
                        .getPhotoTutor(tutor.id, tutor.photoId)
                        .subscribe({
                            next: (response: HttpResponse<IPhoto>) => tutor.photo = response.body.photo,
                            error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                        });
                }
            });
        }
    }

    previousState(): void {
        window.history.back();
    }

    setSubscriptionStatus(status) {
        const modal = this.modalService.open(SubscriptionDialogComponent);
        modal.componentInstance.status = status;
        modal.result.then(result => {
                const currDayJS = dayjs();

                let body = {
                    statusType: status,
                    comments: result.comments,
                    additionalInformation: result.additionalInformation,
                    activationDate: currDayJS,
                    deactivationDate: status == this.activeSubscription ? this.subscription.deactivationDate : currDayJS
                }

                this.childrenService
                    .patchChildItinerarySubscription(this.subscription.id, this.subscription, body)
                    .subscribe({
                        next: () => this.fetchSubscription(this.subscription.id),
                        error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                    })
            },
            () => {
            }
        );
    }

    setAdditionalInformation() {
        const modal = this.modalService.open(AdditionalInformationDialogComponent);
        modal.componentInstance.additionalInformation = this.subscription.additionalInformation;
        modal.result.then(result => {
                let body = {
                    additionalInformation: result.additionalInformation
                }

                this.childrenService
                    .patchChildItinerarySubscription(this.subscription.id, this.subscription, body)
                    .subscribe({
                        next: () => this.fetchSubscription(this.subscription.id),
                        error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                    })
            },
            () => {
            }
        );
    }

    showCommentModal() {
        const modal = this.modalService.open(CommentDialogComponent);
        modal.componentInstance.comment = this.subscription.comments;
    }
}
