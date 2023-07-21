import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILocation, NewLocation } from '../location.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILocation for edit and NewLocationFormGroupInput for create.
 */
type LocationFormGroupInput = ILocation | PartialWithRequiredKeyOf<NewLocation>;

type LocationFormGroupContent = {
  id: FormControl<ILocation['id'] | NewLocation['id']>;
  designation: FormControl<ILocation['designation']>;
  street: FormControl<ILocation['street']>;
  portNumber: FormControl<ILocation['portNumber']>;
  floor: FormControl<ILocation['floor']>;
  postalCode: FormControl<ILocation['postalCode']>;
  city: FormControl<ILocation['city']>;
  country: FormControl<ILocation['country']>;
  type: FormControl<ILocation['type']>;
  longitude: FormControl<ILocation['longitude']>;
  latitude: FormControl<ILocation['latitude']>;
  plusCode: FormControl<ILocation['plusCode']>;
  tutors: FormControl<ILocation['tutors']>;
  organizations: FormControl<ILocation['organizations']>;
};

export type LocationFormGroup = FormGroup<LocationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LocationFormService {
  createLocationFormGroup(location: LocationFormGroupInput = { id: null }): LocationFormGroup {
    const locationRawValue = {
      ...this.getFormDefaults(),
      ...location,
    };
    return new FormGroup<LocationFormGroupContent>({
      id: new FormControl(
        { value: locationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      designation: new FormControl(locationRawValue.designation, {
        validators: [Validators.required],
      }),
      street: new FormControl(locationRawValue.street, {
        validators: [Validators.required],
      }),
      portNumber: new FormControl(locationRawValue.portNumber, {
        validators: [Validators.required],
      }),
      floor: new FormControl(locationRawValue.floor),
      postalCode: new FormControl(locationRawValue.postalCode, {
        validators: [Validators.required],
      }),
      city: new FormControl(locationRawValue.city, {
        validators: [Validators.required],
      }),
      country: new FormControl(locationRawValue.country, {
        validators: [Validators.required],
      }),
      type: new FormControl(locationRawValue.type, {
        validators: [Validators.required],
      }),
      longitude: new FormControl(locationRawValue.longitude, {
        validators: [Validators.required],
      }),
      latitude: new FormControl(locationRawValue.latitude, {
        validators: [Validators.required],
      }),
      plusCode: new FormControl(locationRawValue.plusCode, {
        validators: [Validators.required],
      }),
      tutors: new FormControl(locationRawValue.tutors ?? []),
      organizations: new FormControl(locationRawValue.organizations ?? []),
    });
  }

  getLocation(form: LocationFormGroup): ILocation | NewLocation {
    return form.getRawValue() as ILocation | NewLocation;
  }

  resetForm(form: LocationFormGroup, location: LocationFormGroupInput): void {
    const locationRawValue = { ...this.getFormDefaults(), ...location };
    form.reset(
      {
        ...locationRawValue,
        id: { value: locationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LocationFormDefaults {
    return {
      id: null,
      tutors: [],
      organizations: [],
    };
  }
}
