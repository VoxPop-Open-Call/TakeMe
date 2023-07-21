import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../promoter-itinerary.test-samples';

import { PromoterItineraryFormService } from './promoter-itinerary-form.service';

describe('PromoterItinerary Form Service', () => {
  let service: PromoterItineraryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PromoterItineraryFormService);
  });

  describe('Service methods', () => {
    describe('createPromoterItineraryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPromoterItineraryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            service: expect.any(Object),
          })
        );
      });

      it('passing IPromoterItinerary should create a new form with FormGroup', () => {
        const formGroup = service.createPromoterItineraryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            service: expect.any(Object),
          })
        );
      });
    });

    describe('getPromoterItinerary', () => {
      it('should return NewPromoterItinerary for default PromoterItinerary initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPromoterItineraryFormGroup(sampleWithNewData);

        const promoterItinerary = service.getPromoterItinerary(formGroup) as any;

        expect(promoterItinerary).toMatchObject(sampleWithNewData);
      });

      it('should return NewPromoterItinerary for empty PromoterItinerary initial value', () => {
        const formGroup = service.createPromoterItineraryFormGroup();

        const promoterItinerary = service.getPromoterItinerary(formGroup) as any;

        expect(promoterItinerary).toMatchObject({});
      });

      it('should return IPromoterItinerary', () => {
        const formGroup = service.createPromoterItineraryFormGroup(sampleWithRequiredData);

        const promoterItinerary = service.getPromoterItinerary(formGroup) as any;

        expect(promoterItinerary).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPromoterItinerary should not enable id FormControl', () => {
        const formGroup = service.createPromoterItineraryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPromoterItinerary should disable id FormControl', () => {
        const formGroup = service.createPromoterItineraryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
