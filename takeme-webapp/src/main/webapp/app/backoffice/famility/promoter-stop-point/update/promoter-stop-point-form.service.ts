import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPromoterStopPoint, NewPromoterStopPoint } from '../promoter-stop-point.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPromoterStopPoint for edit and NewPromoterStopPointFormGroupInput for create.
 */
type PromoterStopPointFormGroupInput = IPromoterStopPoint | PartialWithRequiredKeyOf<NewPromoterStopPoint>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPromoterStopPoint | NewPromoterStopPoint> = Omit<T, 'scheduledTime'> & {
  scheduledTime?: string | null;
};

type PromoterStopPointFormRawValue = FormValueOf<IPromoterStopPoint>;

type NewPromoterStopPointFormRawValue = FormValueOf<NewPromoterStopPoint>;

type PromoterStopPointFormDefaults = Pick<NewPromoterStopPoint, 'id' | 'scheduledTime'>;

type PromoterStopPointFormGroupContent = {
  id: FormControl<PromoterStopPointFormRawValue['id'] | NewPromoterStopPoint['id']>;
  name: FormControl<PromoterStopPointFormRawValue['name']>;
  scheduledTime: FormControl<PromoterStopPointFormRawValue['scheduledTime']>;
  promoterItineraryId: FormControl<PromoterStopPointFormRawValue['promoterItineraryId']>;
  location: FormControl<PromoterStopPointFormRawValue['location']>;
};

export type PromoterStopPointFormGroup = FormGroup<PromoterStopPointFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PromoterStopPointFormService {
  createPromoterStopPointFormGroup(promoterStopPoint: PromoterStopPointFormGroupInput = { id: null }): PromoterStopPointFormGroup {
    const promoterStopPointRawValue = this.convertPromoterStopPointToPromoterStopPointRawValue({
      ...this.getFormDefaults(),
      ...promoterStopPoint,
    });
    return new FormGroup<PromoterStopPointFormGroupContent>({
      id: new FormControl(
        { value: promoterStopPointRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(promoterStopPointRawValue.name, {
        validators: [Validators.required],
      }),
      scheduledTime: new FormControl(promoterStopPointRawValue.scheduledTime, {
        validators: [Validators.required],
      }),
      promoterItineraryId: new FormControl(promoterStopPointRawValue.promoterItineraryId),
      location: new FormControl(promoterStopPointRawValue.location),
    });
  }

  getPromoterStopPoint(form: PromoterStopPointFormGroup): IPromoterStopPoint | NewPromoterStopPoint {
    return this.convertPromoterStopPointRawValueToPromoterStopPoint(
      form.getRawValue() as PromoterStopPointFormRawValue | NewPromoterStopPointFormRawValue
    );
  }

  resetForm(form: PromoterStopPointFormGroup, promoterStopPoint: PromoterStopPointFormGroupInput): void {
    const promoterStopPointRawValue = this.convertPromoterStopPointToPromoterStopPointRawValue({
      ...this.getFormDefaults(),
      ...promoterStopPoint,
    });
    form.reset(
      {
        ...promoterStopPointRawValue,
        id: { value: promoterStopPointRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PromoterStopPointFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      scheduledTime: currentTime,
    };
  }

  private convertPromoterStopPointRawValueToPromoterStopPoint(
    rawPromoterStopPoint: PromoterStopPointFormRawValue | NewPromoterStopPointFormRawValue
  ): IPromoterStopPoint | NewPromoterStopPoint {
    return {
      ...rawPromoterStopPoint,
      scheduledTime: dayjs(rawPromoterStopPoint.scheduledTime, DATE_TIME_FORMAT),
    };
  }

  private convertPromoterStopPointToPromoterStopPointRawValue(
    promoterStopPoint: IPromoterStopPoint | (Partial<NewPromoterStopPoint> & PromoterStopPointFormDefaults)
  ): PromoterStopPointFormRawValue | PartialWithRequiredKeyOf<NewPromoterStopPointFormRawValue> {
    return {
      ...promoterStopPoint,
      scheduledTime: promoterStopPoint.scheduledTime ? promoterStopPoint.scheduledTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
