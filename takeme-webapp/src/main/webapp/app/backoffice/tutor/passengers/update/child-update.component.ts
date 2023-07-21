import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {ChildrenService} from "../../../../shared/services/children.service";
import {ChildDTO} from "../../../../shared/model/child-dto";
import {Principal} from "../../../../core";
import {IChild} from "../../../../shared/model/child.model";
import moment from "moment/moment";
import {ErrorHandlerProviderService} from "../../../../shared/providers/error-handler-provider.service";
import {IPhoto} from "../../../../shared/model/photo.model";

@Component({
    selector: 'jhi-tutor-passengers-update',
    templateUrl: './child-update.component.html',
    styles: ['div.ng-invalid { border: transparent }']
})
export class ChildUpdateComponent implements OnInit, OnDestroy {
    child: IChild = null;

    childId: number;
    tutorId: number;

    updateForm: FormGroup;

    constructor(
        private childrenService: ChildrenService,
        private errorHandler: ErrorHandlerProviderService,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private formBuilder: FormBuilder
    ) {
    }

    ngOnInit() {
        this.childId = this.activatedRoute.snapshot.params.id;
        this.tutorId = this.principal.getUserIdentity().tutor.id;

        this.updateForm = this.formBuilder.group({
            child: this.formBuilder.group({
                name: ['', [Validators.required, Validators.pattern("^[a-zA-ZÀ-Ýà-ÿ '´`]*$")]],
                nif: ['', Validators.required],
                dateOfBirth: ['', Validators.required],
                photo: [undefined]
            })
        });

        if (this.childId) {
            this.fetchChild();
        }
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    get form() {
        return this.updateForm.controls;
    }

    save() {
        if (this.child) {
            this.updateChild();
        } else {
            this.createChild();
        }
    }

    private createChild() {
        const child: ChildDTO = new ChildDTO(
            this.form.child.get('name').value,
            'PT',
            this.form.child.get('nif').value,
            moment(this.form.child.get('dateOfBirth').value),
            this.form.child.get('photo').value
        );

        this.childrenService
            .createChildren(this.tutorId, child)
            .subscribe({
                next: () => this.previousState(),
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    private updateChild() {
        this.child.name = this.form.child.get('name').value;
        this.child.nifNumber = this.form.child.get('nif').value;
        this.child.dateOfBirth = this.form.child.get('dateOfBirth').value;
        this.child.photo = this.form.child.get('photo').value;

        this.childrenService
            .update(this.tutorId, this.child)
            .subscribe({
                next: () => this.previousState(),
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    previousState(): void {
        window.history.back();
    }

    onFileChange(event) {
        const reader = new FileReader();

        if (event.target.files && event.target.files.length) {
            const [file] = event.target.files;
            reader.readAsDataURL(file);

            reader.onload = () => {
                const img = reader.result.toString().split(',')[1];
                this.form.child.get('photo').patchValue(img);
                this.form.child.get('photo').markAsDirty();
            };
        }
    }

    private fetchChild() {
        this.childrenService
            .find(this.tutorId, this.childId)

            .subscribe({
                next: (response: HttpResponse<IChild>) => {
                    this.child = response.body;
                    this.fetchPhoto();
                    this.form.child.get('name').patchValue(this.child.name);
                    this.form.child.get('nif').patchValue(this.child.nifNumber);
                    this.form.child.get('dateOfBirth').patchValue(moment(this.child.dateOfBirth));
                },
                error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
            });
    }

    private fetchPhoto() {
        if (this.child.photoId) {
            this.childrenService
                .getChildPhoto(this.child.id, this.child.photoId)
                .subscribe({
                    next: (response: HttpResponse<IPhoto>) => {
                        this.child.photo = response.body.photo;
                        this.form.child.get('photo').patchValue(this.child.photo);
                    },
                    error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                });
        }
    }
}
