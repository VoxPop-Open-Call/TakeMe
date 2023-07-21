import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../child-itinerary-subscription.test-samples';

import { ChildItinerarySubscriptionFormService } from './child-itinerary-subscription-form.service';

describe('ChildItinerarySubscription Form Service', () => {
  let service: ChildItinerarySubscriptionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChildItinerarySubscriptionFormService);
  });

  describe('Service methods', () => {
    describe('createChildItinerarySubscriptionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createChildItinerarySubscriptionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            statusType: expect.any(Object),
            subscriptionDate: expect.any(Object),
            activationDate: expect.any(Object),
            deactivationDate: expect.any(Object),
            comments: expect.any(Object),
            additionalInformation: expect.any(Object),
            cardNumber: expect.any(Object),
            child: expect.any(Object),
            promoterItinerary: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });

      it('passing IChildItinerarySubscription should create a new form with FormGroup', () => {
        const formGroup = service.createChildItinerarySubscriptionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            statusType: expect.any(Object),
            subscriptionDate: expect.any(Object),
            activationDate: expect.any(Object),
            deactivationDate: expect.any(Object),
            comments: expect.any(Object),
            additionalInformation: expect.any(Object),
            cardNumber: expect.any(Object),
            child: expect.any(Object),
            promoterItinerary: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });
    });

    describe('getChildItinerarySubscription', () => {
      it('should return NewChildItinerarySubscription for default ChildItinerarySubscription initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createChildItinerarySubscriptionFormGroup(sampleWithNewData);

        const childItinerarySubscription = service.getChildItinerarySubscription(formGroup) as any;

        expect(childItinerarySubscription).toMatchObject(sampleWithNewData);
      });

      it('should return NewChildItinerarySubscription for empty ChildItinerarySubscription initial value', () => {
        const formGroup = service.createChildItinerarySubscriptionFormGroup();

        const childItinerarySubscription = service.getChildItinerarySubscription(formGroup) as any;

        expect(childItinerarySubscription).toMatchObject({});
      });

      it('should return IChildItinerarySubscription', () => {
        const formGroup = service.createChildItinerarySubscriptionFormGroup(sampleWithRequiredData);

        const childItinerarySubscription = service.getChildItinerarySubscription(formGroup) as any;

        expect(childItinerarySubscription).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IChildItinerarySubscription should not enable id FormControl', () => {
        const formGroup = service.createChildItinerarySubscriptionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewChildItinerarySubscription should disable id FormControl', () => {
        const formGroup = service.createChildItinerarySubscriptionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
