import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { Principal } from 'app/core';
import { ErrorHandlerProviderService } from 'app/shared/providers/error-handler-provider.service';

import { IVehicle } from 'app/shared/model/vehicle.model';
import { IOrganization } from 'app/shared/model/organization.model';
import { OrganizationService } from "app/entities/organization/organization.service";
import { VehicleService } from "app/entities/vehicle/vehicle.service";

@Component({
    selector: 'jhi-update-vehicle',
    templateUrl: './update-vehicle.component.html',
    styles: ['div.ng-invalid { border: transparent }'],
})
export class UpdateVehicleComponent implements OnInit, OnDestroy {
    id;
    vehicle: IVehicle;
    updateForm: FormGroup;
    organization: IOrganization;

    constructor(
        private errorHandler: ErrorHandlerProviderService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private formBuilder: FormBuilder,
        private organizationService: OrganizationService,
        private principal: Principal,
        private vehicleService: VehicleService
    ) {}

    ngOnInit() {
        this.updateForm = this.formBuilder.group({
            vehicle: this.formBuilder.group({
                designation: ['', [Validators.required, Validators.maxLength(30), Validators.minLength(2)]],
                licensePlate: ['', [Validators.maxLength(20), Validators.minLength(2)]],
                capacity: ['', [Validators.required, Validators.pattern('^[0-9]*$'), Validators.minLength(1)]]
            })
        });

        this.id = this.activatedRoute.snapshot.params.id;

        if (this.id) {
            this.loadVehicle();
        } else {
            this.loadOrganization();
        }
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    loadOrganization() {
        this.organizationService.find(+this.principal.getIdOrganization()).subscribe(
            response => {
                this.organization = response.body;
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    loadVehicle() {
        this.vehicleService.find(this.id).subscribe(
            (result: HttpResponse<IVehicle>) => {
                this.vehicle = result.body;

                this.form.vehicle.get('designation').patchValue(this.vehicle.designation);
                this.form.vehicle.get('licensePlate').patchValue(this.vehicle.licensePlate);
                this.form.vehicle.get('capacity').patchValue(this.vehicle.capacity);
            },
            (error: HttpErrorResponse) => {
                this.errorHandler.showError(error);
            }
        );
    }

    get form() {
        return this.updateForm.controls;
    }

    save() {
        if (this.vehicle) {
            this.updateVehicle();
        } else {
            this.createVehicle();
        }
    }

    createVehicle() {
        this.vehicleService
            .create({
                designation: this.form.vehicle.get('designation').value,
                licensePlate: this.form.vehicle.get('licensePlate').value,
                capacity: this.form.vehicle.get('capacity').value,
                organization: this.organization
            })
            .subscribe(
                () => {
                    this.goBack();
                },
                (error: HttpErrorResponse) => {
                    this.errorHandler.showError(error);
                }
            );
    }

    updateVehicle() {
        if (
            this.form.vehicle.get('designation').dirty ||
            this.form.vehicle.get('licensePlate').dirty ||
            this.form.vehicle.get('capacity').dirty
        ) {
            this.vehicle.designation = this.form.vehicle.get('designation').value;
            this.vehicle.licensePlate = this.form.vehicle.get('licensePlate').value;
            this.vehicle.capacity = this.form.vehicle.get('capacity').value;
            this.vehicleService.update(this.vehicle).subscribe(
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
        this.router.navigate(['operator/vehicles']);
    }
}
