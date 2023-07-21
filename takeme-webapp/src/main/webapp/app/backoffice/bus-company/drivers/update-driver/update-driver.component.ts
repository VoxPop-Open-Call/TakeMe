import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { Principal } from 'app/core';
import { PhotoService } from 'app/shared/photo/photo.service';
import { OrganizationsService } from 'app/shared/services/organizations.service';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { IDriver } from 'app/shared/model/driver.model';
import { IOrganization } from 'app/shared/model/organization.model';
import { OrganizationService } from "app/entities/organization/organization.service";
import { DriverService } from "app/entities/driver/driver.service";

@Component({
    selector: 'jhi-update-driver',
    templateUrl: './update-driver.component.html',
    providers: [OrganizationsService],
    styles: []
})
export class UpdateDriverComponent implements OnInit, OnDestroy {
    id;
    driver: IDriver;

    updateForm: FormGroup;

    organization: IOrganization;

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private formBuilder: FormBuilder,
        private photoService: PhotoService,
        private organizationsService: OrganizationsService,
        private organizationService: OrganizationService,
        private principal: Principal,
        private driverService: DriverService
    ) {}

    ngOnInit() {
        this.updateForm = this.formBuilder.group({
            driver: this.formBuilder.group({
                id: [''],
                photo: [undefined],
                name: ['', [Validators.minLength(1), Validators.maxLength(30)]],
                driverLicense: ['', [Validators.minLength(1), Validators.maxLength(30)]]
            })
        });

        this.id = this.activatedRoute.snapshot.params.id;

        if (this.id) {
            this.loadDriver();
        }
        this.loadOrganization();
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadOrganization() {
        this.organizationService.find(+this.principal.getIdOrganization()).subscribe(
            response => {
                this.organization = response.body;
            },
            error => {
                this.errorHandler.showError(error);
            }
        );
    }

    loadDriver() {
        this.driverService.find(this.id).subscribe(
            (result: HttpResponse<IDriver>) => {
                this.driver = result.body;
                if (this.driver.photoId) {
                    this.loadDriverPhoto();
                }
                this.form.driver.get('id').patchValue(this.driver.id);
                this.form.driver.get('name').patchValue(this.driver.name);
                this.form.driver.get('driverLicense').patchValue(this.driver.driverLicense);
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    loadDriverPhoto() {
        if (this.driver.photoId !== null) {
            this.photoService.getPhotoDriver(this.driver.id, this.driver.photoId).subscribe(
                response => {
                    this.driver.photo = response.body.photo;
                    this.form.driver.get('photo').patchValue(this.driver.photo);
                },
                error => {
                    this.errorHandler.showError(error);
                }
            );
        }
    }

    get form() {
        return this.updateForm.controls;
    }

    onFileChange(event) {
        const reader = new FileReader();

        if (event.target.files && event.target.files.length) {
            const [file] = event.target.files;
            reader.readAsDataURL(file);

            reader.onload = () => {
                const img = reader.result.toString().split(',')[1];
                this.form.driver.get('photo').patchValue(img);
                this.form.driver.get('photo').markAsDirty();
            };
        }
    }

    save() {
        if (this.driver) {
            this.updateDriver();
        } else {
            this.createDriver();
        }
    }

    createDriver() {
        this.driverService
            .create({
                name: this.form.driver.get('name').value,
                driverLicense: this.form.driver.get('driverLicense').value,
                organization: this.organization,
                photo: this.form.driver.get('photo').value
            })
            .subscribe(
                response => {
                    this.goBack();
                },
                (error: HttpErrorResponse) => {
                    this.errorHandler.showError(error);
                }
            );
    }

    updateDriver() {
        if (this.form.driver.get('name').dirty || this.form.driver.get('driverLicense').dirty || this.form.driver.get('photo').dirty) {
            this.driver.name = this.form.driver.get('name').value;
            this.driver.driverLicense = this.form.driver.get('driverLicense').value;
            this.driver.photo = this.form.driver.get('photo').value;

            this.driverService.update(this.driver).subscribe(
                () => {
                    this.goBack();
                },
                (error: HttpErrorResponse) => {
                    this.errorHandler.showError(error);
                }
            );
        }
    }

    goBack() {
        this.router.navigate(['operator/monitors']);
    }
}
