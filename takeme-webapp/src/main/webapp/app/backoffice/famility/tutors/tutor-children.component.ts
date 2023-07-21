import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { TutorsService } from './tutors.service';
import { PhotoService } from 'app/shared/photo/photo.service';
import { BuildAddress } from 'app/shared/util/build-address';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { IChild } from 'app/shared/model/child.model';
import { ILocation } from 'app/shared/model/location.model';

@Component({
    selector: 'jhi-tutor-children',
    templateUrl: './tutor-children.component.html',
    styles: []
})
export class TutorChildrenComponent implements OnInit, OnDestroy {
    id;
    uid;
    children: IChild[] = [];

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private photoService: PhotoService,
        private activateRoute: ActivatedRoute,
        private tutorsService: TutorsService
    ) {}

    ngOnInit() {
        this.activateRoute.params.subscribe(
            params => {
                this.id = params['id'];
                this.uid = params['uid'];

                this.tutorsService.getChildrenByTutor(this.id).subscribe(
                    result => {
                        this.children = result.body;
                        this.loadChildrenSubscriptions();
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

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadChildrenSubscriptions() {
        this.children.forEach(child => {
            this.tutorsService.getOrganizationOfChild(child.id).subscribe(
                result => {
                    child.childSubscriptionChild = result.body;
                    this.loadPhotos();
                },
                error => {
                    this.errorHandler.showError(error);
                }
            );
        });
    }

    loadPhotos() {
        this.children.forEach(child => {
            if (child.photoId !== null) {
                this.photoService.getPhotoChild(child.id, child.photoId).subscribe(
                    result => {
                        child.photo = result.body.photo;
                    },
                    error => {
                        this.errorHandler.showError(error);
                    }
                );
            }
        });
    }

    goBack() {
        window.history.back();
    }

    buildAddress(location: ILocation) {
        return BuildAddress.buildAddressFromLocationObject(location);
    }
}
