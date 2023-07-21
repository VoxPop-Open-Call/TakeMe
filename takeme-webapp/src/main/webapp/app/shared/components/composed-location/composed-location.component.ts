import {Component, Input, OnInit} from '@angular/core';
import {AbstractControl} from '@angular/forms';
import {IServiceStopPoint} from 'app/shared/model/service-stop-point.model';

@Component({
    selector: 'jhi-composed-location',
    templateUrl: './composed-location.component.html'
})
export class ComposedLocationComponent implements OnInit {
    @Input()
    pointForm: AbstractControl;

    @Input()
    point: IServiceStopPoint;

    constructor() {
    }

    ngOnInit() {
    }

    getLocationType(): string {
        return this.pointForm ? this.pointForm.get('location.type').value : this.point.location.type;
    }

    getLocationDesignation() {
        return this.pointForm ? this.pointForm.get('location.designation').value : this.point.location.designation;
    }

    getLocationComposedAddress() {
        let street = this.pointForm ? this.pointForm.get('location.street').value : this.point.location.street;
        let doorNumber = this.pointForm ? this.pointForm.get('location.portNumber').value : this.point.location.portNumber;
        let floor = this.pointForm ? this.pointForm.get('location.floor').value : this.point.location.floor;
        let city = this.pointForm ? this.pointForm.get('location.city').value : this.point.location.city;
        let postalCode = this.pointForm ? this.pointForm.get('location.postalCode').value : this.point.location.postalCode;
        let country = this.pointForm ? this.pointForm.get('location.country').value : this.point.location.country;

        let composedAddress = `${street}, ${doorNumber}`;
        if (floor) composedAddress = `${composedAddress}, ${floor}`;
        composedAddress = `${composedAddress}, ${city}, ${postalCode}, ${country}`;

        return composedAddress;
    }

    getLocationLatitude() {
        return this.pointForm ? this.pointForm.get('location.latitude').value : this.point.location.latitude;
    }

    getLocationLongitude() {
        return this.pointForm ? this.pointForm.get('location.longitude').value : this.point.location.longitude;
    }

    getLocationPlusCode() {
        return this.pointForm ? this.pointForm.get('location.plusCode').value : this.point.location.plusCode;
    }
}
