import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IChildItinerarySubscription } from '../child-itinerary-subscription.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../child-itinerary-subscription.test-samples';

import { ChildItinerarySubscriptionService, RestChildItinerarySubscription } from './child-itinerary-subscription.service';

const requireRestSample: RestChildItinerarySubscription = {
  ...sampleWithRequiredData,
  subscriptionDate: sampleWithRequiredData.subscriptionDate?.toJSON(),
  activationDate: sampleWithRequiredData.activationDate?.toJSON(),
  deactivationDate: sampleWithRequiredData.deactivationDate?.toJSON(),
};

describe('ChildItinerarySubscription Service', () => {
  let service: ChildItinerarySubscriptionService;
  let httpMock: HttpTestingController;
  let expectedResult: IChildItinerarySubscription | IChildItinerarySubscription[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ChildItinerarySubscriptionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ChildItinerarySubscription', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const childItinerarySubscription = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(childItinerarySubscription).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ChildItinerarySubscription', () => {
      const childItinerarySubscription = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(childItinerarySubscription).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ChildItinerarySubscription', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ChildItinerarySubscription', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ChildItinerarySubscription', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addChildItinerarySubscriptionToCollectionIfMissing', () => {
      it('should add a ChildItinerarySubscription to an empty array', () => {
        const childItinerarySubscription: IChildItinerarySubscription = sampleWithRequiredData;
        expectedResult = service.addChildItinerarySubscriptionToCollectionIfMissing([], childItinerarySubscription);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(childItinerarySubscription);
      });

      it('should not add a ChildItinerarySubscription to an array that contains it', () => {
        const childItinerarySubscription: IChildItinerarySubscription = sampleWithRequiredData;
        const childItinerarySubscriptionCollection: IChildItinerarySubscription[] = [
          {
            ...childItinerarySubscription,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addChildItinerarySubscriptionToCollectionIfMissing(
          childItinerarySubscriptionCollection,
          childItinerarySubscription
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ChildItinerarySubscription to an array that doesn't contain it", () => {
        const childItinerarySubscription: IChildItinerarySubscription = sampleWithRequiredData;
        const childItinerarySubscriptionCollection: IChildItinerarySubscription[] = [sampleWithPartialData];
        expectedResult = service.addChildItinerarySubscriptionToCollectionIfMissing(
          childItinerarySubscriptionCollection,
          childItinerarySubscription
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(childItinerarySubscription);
      });

      it('should add only unique ChildItinerarySubscription to an array', () => {
        const childItinerarySubscriptionArray: IChildItinerarySubscription[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const childItinerarySubscriptionCollection: IChildItinerarySubscription[] = [sampleWithRequiredData];
        expectedResult = service.addChildItinerarySubscriptionToCollectionIfMissing(
          childItinerarySubscriptionCollection,
          ...childItinerarySubscriptionArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const childItinerarySubscription: IChildItinerarySubscription = sampleWithRequiredData;
        const childItinerarySubscription2: IChildItinerarySubscription = sampleWithPartialData;
        expectedResult = service.addChildItinerarySubscriptionToCollectionIfMissing(
          [],
          childItinerarySubscription,
          childItinerarySubscription2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(childItinerarySubscription);
        expect(expectedResult).toContain(childItinerarySubscription2);
      });

      it('should accept null and undefined values', () => {
        const childItinerarySubscription: IChildItinerarySubscription = sampleWithRequiredData;
        expectedResult = service.addChildItinerarySubscriptionToCollectionIfMissing([], null, childItinerarySubscription, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(childItinerarySubscription);
      });

      it('should return initial array if no ChildItinerarySubscription is added', () => {
        const childItinerarySubscriptionCollection: IChildItinerarySubscription[] = [sampleWithRequiredData];
        expectedResult = service.addChildItinerarySubscriptionToCollectionIfMissing(childItinerarySubscriptionCollection, undefined, null);
        expect(expectedResult).toEqual(childItinerarySubscriptionCollection);
      });
    });

    describe('compareChildItinerarySubscription', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareChildItinerarySubscription(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareChildItinerarySubscription(entity1, entity2);
        const compareResult2 = service.compareChildItinerarySubscription(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareChildItinerarySubscription(entity1, entity2);
        const compareResult2 = service.compareChildItinerarySubscription(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareChildItinerarySubscription(entity1, entity2);
        const compareResult2 = service.compareChildItinerarySubscription(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
