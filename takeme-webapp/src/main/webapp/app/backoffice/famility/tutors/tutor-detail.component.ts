import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { TutorsService } from './tutors.service';
import { PhotoService } from 'app/shared/photo/photo.service';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { ITutor } from 'app/shared/model/tutor.model';

@Component({
    selector: 'jhi-tutor-detail',
    templateUrl: './tutor-detail.component.html',
    styles: []
})
export class TutorDetailComponent implements OnInit, OnDestroy {
    id;
    photo;
    tutor: ITutor;

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private router: Router,
        private photoService: PhotoService,
        private activatedRoute: ActivatedRoute,
        private tutorsService: TutorsService
    ) {}

    ngOnInit() {
        this.loadTutor();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadTutor() {
        this.activatedRoute.params.subscribe(
            params => {
                this.id = params['id'];
                this.tutorsService.getTutor(this.id).subscribe(
                    result => {
                        this.tutor = result.body;
                        this.loadPhoto();
                    },
                    error => {
                        this.errorHandler.showError(error);
                    }
                );
            },
            error => {
                this.errorHandler.showError(error);
            }
        );
    }

    loadPhoto() {
        if (this.tutor.photoId !== null) {
            this.photoService.getPhotoTutor(this.tutor.id, this.tutor.photoId).subscribe(
                result => {
                    this.photo = result.body.photo;
                },
                error => {
                    this.errorHandler.showError(error);
                }
            );
        }
    }

    goToTutorChildren() {
        this.router.navigate(['promoter/tutors/' + this.id + '/children']);
    }

    goBack() {
        window.history.back();
    }
}
