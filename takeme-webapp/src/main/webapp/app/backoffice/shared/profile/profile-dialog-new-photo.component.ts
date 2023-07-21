import { Component, ElementRef, Input, ViewChild, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';
import { OrganizationService } from "../../../entities/organization/organization.service";

@Component({
    selector: 'jhi-profile-dialog-new-photo',
    templateUrl: './profile-dialog-new-photo.component.html'
})
export class ProfileDialogNewPhotoComponent implements OnDestroy {
    @Input()
    idOrganization: number;

    form: FormGroup;
    loading = false;
    newPhoto;

    @ViewChild('fileInput')
    fileInput: ElementRef;

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        public activeModal: NgbActiveModal,
        private fb: FormBuilder,
        private organizationService: OrganizationService
    ) {
        this.createForm();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    createForm() {
        this.form = this.fb.group({
            avatar: null
        });
    }

    onFileChange(event) {
        const reader = new FileReader();
        if (event.target.files && event.target.files.length > 0) {
            const file = event.target.files[0];
            reader.readAsDataURL(file);
            reader.onload = () => {
                const photo64 = reader.result.toString();
                this.newPhoto = photo64.slice(photo64.indexOf(',') + 1);
            };
        }
    }

    onSubmit() {
        this.loading = true;

        this.organizationService.patchPhoto(this.idOrganization, this.newPhoto).subscribe(
            (result: HttpResponse<any>) => {
                this.loading = false;
                this.activeModal.close('Ok');
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }
}
