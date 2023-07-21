import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../location.test-samples';

import { LocationFormService } from './location-form.service';

describe('Location Form Service', () => {
  let service: LocationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LocationFormService);
  });

  describe('Service methods', () => {
    describe('createLocationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLocationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            designation: expect.any(Object),
            street: expect.any(Object),
            portNumber: expect.any(Object),
            floor: expect.any(Object),
            postalCode: expect.any(Object),
            city: expect.any(Object),
            country: expect.any(Object),
            type: expect.any(Object),
            longitude: expect.any(Object),
            latitude: expect.any(Object),
            plusCode: expect.any(Object),
            tutors: expect.any(Object),
            organizations: expect.any(Object),
          })
        );
      });

      it('passing ILocation should create a new form with FormGroup', () => {
        const formGroup = service.createLocationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            designation: expect.any(Object),
            street: expect.any(Object),
            portNumber: expect.any(Object),
            floor: expect.any(Object),
            postalCode: expect.any(Object),
            city: expect.any(Object),
            country: expect.any(Object),
            type: expect.any(Object),
            longitude: expect.any(Object),
            latitude: expect.any(Object),
            plusCode: expect.any(Object),
            tutors: expect.any(Object),
            organizations: expect.any(Object),
          })
        );
      });
    });

    describe('getLocation', () => {
      it('should return NewLocation for default Location initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createLocationFormGroup(sampleWithNewData);

        const location = service.getLocation(formGroup) as any;

        expect(location).toMatchObject(sampleWithNewData);
      });

      it('should return NewLocation for empty Location initial value', () => {
        const formGroup = service.createLocationFormGroup();

        const location = service.getLocation(formGroup) as any;

        expect(location).toMatchObject({});
      });

      it('should return ILocation', () => {
        const formGroup = service.createLocationFormGroup(sampleWithRequiredData);

        const location = service.getLocation(formGroup) as any;

        expect(location).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILocation should not enable id FormControl', () => {
        const formGroup = service.createLocationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLocation should disable id FormControl', () => {
        const formGroup = service.createLocationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
