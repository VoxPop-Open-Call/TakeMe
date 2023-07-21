import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {TutorsService} from './tutors.service';
import {TotalElementsHeader} from 'app/shared/util/total-elements-header';
import {ErrorHandlerProviderService} from 'app/shared/providers/error-handler-provider.service';
import {ITutor} from 'app/shared/model/tutor.model';
import {TutorChangeStatusDialogComponent} from 'app/backoffice/famility/tutors/tutor-change-status-dialog.component';
import {debounceTime, Subject} from "rxjs";
import {DEBOUNCE_TIME} from "../../../config/input.constants";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {IPhoto} from "../../../shared/model/photo.model";
import {PhotoService} from "../../../shared/photo/photo.service";
import {TutorsFilterType} from "./tutors-filter.service";

@Component({
    selector: 'jhi-famility-tutors-list',
    templateUrl: './tutors.component.html'
})
export class TutorsComponent implements OnInit, OnDestroy {
    tutors: ITutor[] = [];

    activeTutor = TutorsFilterType.ACTIVE;
    inactiveTutor = TutorsFilterType.INACTIVE;

    showActive = true;
    showInactive = false;

    tutorFilter = '';

    totalItems = 0;
    page = 0;
    itemsPerPage = 5;
    sortProperty = 'name';
    sortDirection = 'desc';

    textSearchUpdate = new Subject<string>();

    constructor(
        private tutorsService: TutorsService,
        private photoService: PhotoService,
        private errorHandler: ErrorHandlerProviderService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private totalElementsHeader: TotalElementsHeader,
        private modalService: NgbModal
    ) {
        this.textSearchUpdate.pipe(debounceTime(DEBOUNCE_TIME)).subscribe(() => this.filterUpdate())
    }

    ngOnInit() {
        this.activatedRoute.queryParams.subscribe(params => {
            if (params.name) {
                this.tutorFilter = params.name;
            }

            if (params.active) {
                if (params.active == 'true') {
                    this.showActive = true;
                    this.showInactive = false;
                } else {
                    this.showActive = false;
                    this.showInactive = true;
                }
            }

            if (params.page) {
                this.page = params.page - 1;
            }

            this.fetchTutors();
        });
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    private fetchTutors() {
        let request = {
            tutorName: this.tutorFilter,
            tutorStatus: this.showActive ? this.activeTutor : this.inactiveTutor,
        }

        let pagination = {
            page: this.page,
            itemsPerPage: this.itemsPerPage,
            sortProperty: this.sortProperty,
            sortDirection: this.sortDirection
        }

        this.tutorsService
            .getTutors(request, pagination)
            .subscribe({
                next: (response: HttpResponse<ITutor[]>) => {
                    this.tutors = response.body;
                    this.totalItems = this.totalElementsHeader.getTotalElements(response.headers);
                    this.fetchPhotos();
                },
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    private fetchPhotos() {
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

    showActiveTutors() {
        this.page = 0;
        this.showActive = true;
        this.showInactive = false;
        this.filterUpdate();
    }

    showInactiveTutors() {
        this.page = 0;
        this.showActive = false;
        this.showInactive = true;
        this.filterUpdate();
    }

    goToDetailed(id) {
        this.router.navigate(['promoter/tutors', id]).then();
    }

    setTutorStatus(id, name, status) {
        const modal = this.modalService.open(TutorChangeStatusDialogComponent);
        modal.componentInstance.name = name;
        modal.componentInstance.newStatus = status;
        modal.result.then(() => {
            this.tutorsService
                .setTutorStatus(id, status)
                .subscribe({
                    next: () => this.fetchTutors(),
                    error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                })
        });
    }

    navigateToPage($event) {
        this.page = $event - 1;
        this.filterUpdate();
    }

    private filterUpdate() {
        this.router.navigate(['promoter/tutors'], {
            queryParams: {
                name: this.tutorFilter == '' ? null : this.tutorFilter,
                active: this.showActive,
                page: this.page + 1
            }
        }).then()
    }

    resetFilter() {
        this.tutorFilter = '';
        this.filterUpdate();
    }
}
