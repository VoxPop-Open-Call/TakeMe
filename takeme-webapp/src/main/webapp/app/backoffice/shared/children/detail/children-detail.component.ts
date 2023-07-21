import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {PhotoService} from 'app/shared/photo/photo.service';
import {ErrorHandlerProviderService} from 'app/shared/providers/error-handler-provider.service';
import {IChildItinerarySubscription} from "../../../../entities/child-itinerary-subscription/child-itinerary-subscription.model";
import {ChildrenService} from "../../../../shared/services/children.service";
import {ITutor} from "../../../../shared/model/tutor.model";
import {IPhoto} from "../../../../shared/model/photo.model";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {CommentDialogComponent} from "./comment-dialog/comment-dialog.component";
import {AdditionalInformationDialogComponent} from "./additional-information-dialog/additional-information-dialog.component";
import {StatusType} from "../../../../shared/model/child.model";
import {Principal} from "../../../../core";
import {Authority} from "../../../../config/authority.constants";

@Component({
    selector: 'jhi-bus-company-passengers-detail',
    templateUrl: './children-detail.component.html'
})
export class ChildrenDetailComponent implements OnInit, OnDestroy {
    subscription: IChildItinerarySubscription = null;
    tutors: ITutor[] = [];
    childSubscriptions: IChildItinerarySubscription[] = [];

    isOperator;

    constructor(
        private childrenService: ChildrenService,
        private photoService: PhotoService,
        private errorHandler: ErrorHandlerProviderService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private principal: Principal,
        private modalService: NgbModal
    ) {
    }

    ngOnInit() {
        this.principal
            .hasAuthority(Authority.OPERATOR)
            .then(isOperator => {
                this.isOperator = isOperator;

                this.activatedRoute.params.subscribe(params => this.fetchSubscription(params['id']));
            });
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    private fetchSubscription(subscriptionId) {
        this.childrenService
            .getChildItinerarySubscription(subscriptionId)
            .subscribe({
                next: (response: HttpResponse<IChildItinerarySubscription>) => {
                    this.subscription = response.body;
                    this.fetchTutors();
                    this.fetchChildSubscriptions();
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

    private fetchChildSubscriptions() {
        let request = {
            operatorId: !this.isOperator ? null : this.principal.getIdOrganization(),
            statusType: StatusType.ACTIVE,
            childId: this.subscription.child.id
        }

        this.childrenService
            .getChildItinerarySubscriptions(request)
            .subscribe({
                next: (response: HttpResponse<IChildItinerarySubscription[]>) => this.childSubscriptions = response.body,
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

    showCommentModal() {
        const modal = this.modalService.open(CommentDialogComponent);
        modal.componentInstance.comment = this.subscription.comments;
    }

    showAdditionalInformation() {
        const modal = this.modalService.open(AdditionalInformationDialogComponent);
        modal.componentInstance.additionalInformation = this.subscription.additionalInformation;
    }

    goToDetailed(id) {
        if (!this.isOperator) {
            this.router.navigate(['promoter/passengers', id]).then();
        } else {
            this.router.navigate(['operator/passengers', id]).then();
        }
    }

    goToServices(id) {
        if (!this.isOperator) {
            this.router.navigate(['promoter/passengers', id, 'services']).then();
        } else {
            this.router.navigate(['operator/passengers', id, 'services']).then();
        }
    }
}
