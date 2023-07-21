import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPromoterItinerary, NewPromoterItinerary } from '../promoter-itinerary.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPromoterItinerary for edit and NewPromoterItineraryFormGroupInput for create.
 */
type PromoterItineraryFormGroupInput = IPromoterItinerary | PartialWithRequiredKeyOf<NewPromoterItinerary>;

type PromoterItineraryFormDefaults = Pick<NewPromoterItinerary, 'id'>;

type PromoterItineraryFormGroupContent = {
  id: FormControl<IPromoterItinerary['id'] | NewPromoterItinerary['id']>;
  name: FormControl<IPromoterItinerary['name']>;
  startDate: FormControl<IPromoterItinerary['startDate']>;
  endDate: FormControl<IPromoterItinerary['endDate']>;
  service: FormControl<IPromoterItinerary['service']>;
  organization: FormControl<IPromoterItinerary['organization']>;
  promoterItineraryStopPoints: FormControl<IPromoterItinerary['promoterItineraryStopPoints']>;
};

export type PromoterItineraryFormGroup = FormGroup<PromoterItineraryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PromoterItineraryFormService {
  createPromoterItineraryFormGroup(promoterItinerary: PromoterItineraryFormGroupInput = { id: null }): PromoterItineraryFormGroup {
    const promoterItineraryRawValue = {
      ...this.getFormDefaults(),
      ...promoterItinerary,
    };
    return new FormGroup<PromoterItineraryFormGroupContent>({
      id: new FormControl(
        { value: promoterItineraryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(promoterItineraryRawValue.name, {
        validators: [Validators.required],
      }),
      startDate: new FormControl(promoterItineraryRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(promoterItineraryRawValue.endDate),
      service: new FormControl(promoterItineraryRawValue.service, {
        validators: [Validators.required],
      }),
      organization: new FormControl(promoterItineraryRawValue.organization),
      promoterItineraryStopPoints: new FormControl(promoterItineraryRawValue.promoterItineraryStopPoints),
    });
  }

  getPromoterItinerary(form: PromoterItineraryFormGroup): IPromoterItinerary | NewPromoterItinerary {
    return form.getRawValue() as IPromoterItinerary | NewPromoterItinerary;
  }

  resetForm(form: PromoterItineraryFormGroup, promoterItinerary: PromoterItineraryFormGroupInput): void {
    const promoterItineraryRawValue = { ...this.getFormDefaults(), ...promoterItinerary };
    form.reset(
      {
        ...promoterItineraryRawValue,
        id: { value: promoterItineraryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PromoterItineraryFormDefaults {
    return {
      id: null,
    };
  }
}
