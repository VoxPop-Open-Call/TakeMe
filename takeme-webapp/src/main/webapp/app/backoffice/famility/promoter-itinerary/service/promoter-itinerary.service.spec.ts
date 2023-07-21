import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPromoterItinerary } from '../promoter-itinerary.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../promoter-itinerary.test-samples';

import { PromoterItineraryService, RestPromoterItinerary } from './promoter-itinerary.service';

const requireRestSample: RestPromoterItinerary = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('PromoterItinerary Service', () => {
  let service: PromoterItineraryService;
  let httpMock: HttpTestingController;
  let expectedResult: IPromoterItinerary | IPromoterItinerary[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PromoterItineraryService);
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

    it('should create a PromoterItinerary', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const promoterItinerary = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(promoterItinerary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PromoterItinerary', () => {
      const promoterItinerary = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(promoterItinerary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PromoterItinerary', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PromoterItinerary', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PromoterItinerary', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPromoterItineraryToCollectionIfMissing', () => {
      it('should add a PromoterItinerary to an empty array', () => {
        const promoterItinerary: IPromoterItinerary = sampleWithRequiredData;
        expectedResult = service.addPromoterItineraryToCollectionIfMissing([], promoterItinerary);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(promoterItinerary);
      });

      it('should not add a PromoterItinerary to an array that contains it', () => {
        const promoterItinerary: IPromoterItinerary = sampleWithRequiredData;
        const promoterItineraryCollection: IPromoterItinerary[] = [
          {
            ...promoterItinerary,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPromoterItineraryToCollectionIfMissing(promoterItineraryCollection, promoterItinerary);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PromoterItinerary to an array that doesn't contain it", () => {
        const promoterItinerary: IPromoterItinerary = sampleWithRequiredData;
        const promoterItineraryCollection: IPromoterItinerary[] = [sampleWithPartialData];
        expectedResult = service.addPromoterItineraryToCollectionIfMissing(promoterItineraryCollection, promoterItinerary);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(promoterItinerary);
      });

      it('should add only unique PromoterItinerary to an array', () => {
        const promoterItineraryArray: IPromoterItinerary[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const promoterItineraryCollection: IPromoterItinerary[] = [sampleWithRequiredData];
        expectedResult = service.addPromoterItineraryToCollectionIfMissing(promoterItineraryCollection, ...promoterItineraryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const promoterItinerary: IPromoterItinerary = sampleWithRequiredData;
        const promoterItinerary2: IPromoterItinerary = sampleWithPartialData;
        expectedResult = service.addPromoterItineraryToCollectionIfMissing([], promoterItinerary, promoterItinerary2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(promoterItinerary);
        expect(expectedResult).toContain(promoterItinerary2);
      });

      it('should accept null and undefined values', () => {
        const promoterItinerary: IPromoterItinerary = sampleWithRequiredData;
        expectedResult = service.addPromoterItineraryToCollectionIfMissing([], null, promoterItinerary, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(promoterItinerary);
      });

      it('should return initial array if no PromoterItinerary is added', () => {
        const promoterItineraryCollection: IPromoterItinerary[] = [sampleWithRequiredData];
        expectedResult = service.addPromoterItineraryToCollectionIfMissing(promoterItineraryCollection, undefined, null);
        expect(expectedResult).toEqual(promoterItineraryCollection);
      });
    });

    describe('comparePromoterItinerary', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePromoterItinerary(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePromoterItinerary(entity1, entity2);
        const compareResult2 = service.comparePromoterItinerary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePromoterItinerary(entity1, entity2);
        const compareResult2 = service.comparePromoterItinerary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePromoterItinerary(entity1, entity2);
        const compareResult2 = service.comparePromoterItinerary(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
