import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IChildItinerarySubscription, NewChildItinerarySubscription } from '../child-itinerary-subscription.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IChildItinerarySubscription for edit and NewChildItinerarySubscriptionFormGroupInput for create.
 */
type ChildItinerarySubscriptionFormGroupInput = IChildItinerarySubscription | PartialWithRequiredKeyOf<NewChildItinerarySubscription>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IChildItinerarySubscription | NewChildItinerarySubscription> = Omit<
  T,
  'subscriptionDate' | 'activationDate' | 'deactivationDate'
> & {
  subscriptionDate?: string | null;
  activationDate?: string | null;
  deactivationDate?: string | null;
};

type ChildItinerarySubscriptionFormRawValue = FormValueOf<IChildItinerarySubscription>;

type NewChildItinerarySubscriptionFormRawValue = FormValueOf<NewChildItinerarySubscription>;

type ChildItinerarySubscriptionFormDefaults = Pick<
  NewChildItinerarySubscription,
  'id' | 'subscriptionDate' | 'activationDate' | 'deactivationDate'
>;

type ChildItinerarySubscriptionFormGroupContent = {
  id: FormControl<ChildItinerarySubscriptionFormRawValue['id'] | NewChildItinerarySubscription['id']>;
  statusType: FormControl<ChildItinerarySubscriptionFormRawValue['statusType']>;
  subscriptionDate: FormControl<ChildItinerarySubscriptionFormRawValue['subscriptionDate']>;
  activationDate: FormControl<ChildItinerarySubscriptionFormRawValue['activationDate']>;
  deactivationDate: FormControl<ChildItinerarySubscriptionFormRawValue['deactivationDate']>;
  comments: FormControl<ChildItinerarySubscriptionFormRawValue['comments']>;
  additionalInformation: FormControl<ChildItinerarySubscriptionFormRawValue['additionalInformation']>;
  cardNumber: FormControl<ChildItinerarySubscriptionFormRawValue['cardNumber']>;
  child: FormControl<ChildItinerarySubscriptionFormRawValue['child']>;
  promoterItinerary: FormControl<ChildItinerarySubscriptionFormRawValue['promoterItinerary']>;
  user: FormControl<ChildItinerarySubscriptionFormRawValue['user']>;
};

export type ChildItinerarySubscriptionFormGroup = FormGroup<ChildItinerarySubscriptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ChildItinerarySubscriptionFormService {
  createChildItinerarySubscriptionFormGroup(
    childItinerarySubscription: ChildItinerarySubscriptionFormGroupInput = { id: null }
  ): ChildItinerarySubscriptionFormGroup {
    const childItinerarySubscriptionRawValue = this.convertChildItinerarySubscriptionToChildItinerarySubscriptionRawValue({
      ...this.getFormDefaults(),
      ...childItinerarySubscription,
    });
    return new FormGroup<ChildItinerarySubscriptionFormGroupContent>({
      id: new FormControl(
        { value: childItinerarySubscriptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      statusType: new FormControl(childItinerarySubscriptionRawValue.statusType, {
        validators: [Validators.required],
      }),
      subscriptionDate: new FormControl(childItinerarySubscriptionRawValue.subscriptionDate, {
        validators: [Validators.required],
      }),
      activationDate: new FormControl(childItinerarySubscriptionRawValue.activationDate, {
        validators: [Validators.required],
      }),
      deactivationDate: new FormControl(childItinerarySubscriptionRawValue.deactivationDate),
      comments: new FormControl(childItinerarySubscriptionRawValue.comments),
      additionalInformation: new FormControl(childItinerarySubscriptionRawValue.additionalInformation),
      cardNumber: new FormControl(childItinerarySubscriptionRawValue.cardNumber),
      child: new FormControl(childItinerarySubscriptionRawValue.child, {
        validators: [Validators.required],
      }),
      promoterItinerary: new FormControl(childItinerarySubscriptionRawValue.promoterItinerary, {
        validators: [Validators.required],
      }),
      user: new FormControl(childItinerarySubscriptionRawValue.user),
    });
  }

  getChildItinerarySubscription(form: ChildItinerarySubscriptionFormGroup): IChildItinerarySubscription | NewChildItinerarySubscription {
    return this.convertChildItinerarySubscriptionRawValueToChildItinerarySubscription(
      form.getRawValue() as ChildItinerarySubscriptionFormRawValue | NewChildItinerarySubscriptionFormRawValue
    );
  }

  resetForm(form: ChildItinerarySubscriptionFormGroup, childItinerarySubscription: ChildItinerarySubscriptionFormGroupInput): void {
    const childItinerarySubscriptionRawValue = this.convertChildItinerarySubscriptionToChildItinerarySubscriptionRawValue({
      ...this.getFormDefaults(),
      ...childItinerarySubscription,
    });
    form.reset(
      {
        ...childItinerarySubscriptionRawValue,
        id: { value: childItinerarySubscriptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ChildItinerarySubscriptionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      subscriptionDate: currentTime,
      activationDate: currentTime,
      deactivationDate: currentTime,
    };
  }

  private convertChildItinerarySubscriptionRawValueToChildItinerarySubscription(
    rawChildItinerarySubscription: ChildItinerarySubscriptionFormRawValue | NewChildItinerarySubscriptionFormRawValue
  ): IChildItinerarySubscription | NewChildItinerarySubscription {
    return {
      ...rawChildItinerarySubscription,
      subscriptionDate: dayjs(rawChildItinerarySubscription.subscriptionDate, DATE_TIME_FORMAT),
      activationDate: dayjs(rawChildItinerarySubscription.activationDate, DATE_TIME_FORMAT),
      deactivationDate: dayjs(rawChildItinerarySubscription.deactivationDate, DATE_TIME_FORMAT),
    };
  }

  private convertChildItinerarySubscriptionToChildItinerarySubscriptionRawValue(
    childItinerarySubscription:
      | IChildItinerarySubscription
      | (Partial<NewChildItinerarySubscription> & ChildItinerarySubscriptionFormDefaults)
  ): ChildItinerarySubscriptionFormRawValue | PartialWithRequiredKeyOf<NewChildItinerarySubscriptionFormRawValue> {
    return {
      ...childItinerarySubscription,
      subscriptionDate: childItinerarySubscription.subscriptionDate
        ? childItinerarySubscription.subscriptionDate.format(DATE_TIME_FORMAT)
        : undefined,
      activationDate: childItinerarySubscription.activationDate
        ? childItinerarySubscription.activationDate.format(DATE_TIME_FORMAT)
        : undefined,
      deactivationDate: childItinerarySubscription.deactivationDate
        ? childItinerarySubscription.deactivationDate.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
