import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../promoter-stop-point.test-samples';

import { PromoterStopPointFormService } from './promoter-stop-point-form.service';

describe('PromoterStopPoint Form Service', () => {
  let service: PromoterStopPointFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PromoterStopPointFormService);
  });

  describe('Service methods', () => {
    describe('createPromoterStopPointFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPromoterStopPointFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            scheduledTime: expect.any(Object),
            promoterItinerary: expect.any(Object),
            location: expect.any(Object),
          })
        );
      });

      it('passing IPromoterStopPoint should create a new form with FormGroup', () => {
        const formGroup = service.createPromoterStopPointFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            scheduledTime: expect.any(Object),
            promoterItinerary: expect.any(Object),
            location: expect.any(Object),
          })
        );
      });
    });

    describe('getPromoterStopPoint', () => {
      it('should return NewPromoterStopPoint for default PromoterStopPoint initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPromoterStopPointFormGroup(sampleWithNewData);

        const promoterStopPoint = service.getPromoterStopPoint(formGroup) as any;

        expect(promoterStopPoint).toMatchObject(sampleWithNewData);
      });

      it('should return NewPromoterStopPoint for empty PromoterStopPoint initial value', () => {
        const formGroup = service.createPromoterStopPointFormGroup();

        const promoterStopPoint = service.getPromoterStopPoint(formGroup) as any;

        expect(promoterStopPoint).toMatchObject({});
      });

      it('should return IPromoterStopPoint', () => {
        const formGroup = service.createPromoterStopPointFormGroup(sampleWithRequiredData);

        const promoterStopPoint = service.getPromoterStopPoint(formGroup) as any;

        expect(promoterStopPoint).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPromoterStopPoint should not enable id FormControl', () => {
        const formGroup = service.createPromoterStopPointFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPromoterStopPoint should disable id FormControl', () => {
        const formGroup = service.createPromoterStopPointFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
