import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPromoterStopPoint } from '../promoter-stop-point.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../promoter-stop-point.test-samples';

import { PromoterStopPointService, RestPromoterStopPoint } from './promoter-stop-point.service';

const requireRestSample: RestPromoterStopPoint = {
  ...sampleWithRequiredData,
  scheduledTime: sampleWithRequiredData.scheduledTime?.toJSON(),
};

describe('PromoterStopPoint Service', () => {
  let service: PromoterStopPointService;
  let httpMock: HttpTestingController;
  let expectedResult: IPromoterStopPoint | IPromoterStopPoint[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PromoterStopPointService);
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

    it('should create a PromoterStopPoint', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const promoterStopPoint = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(promoterStopPoint).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PromoterStopPoint', () => {
      const promoterStopPoint = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(promoterStopPoint).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PromoterStopPoint', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PromoterStopPoint', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PromoterStopPoint', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPromoterStopPointToCollectionIfMissing', () => {
      it('should add a PromoterStopPoint to an empty array', () => {
        const promoterStopPoint: IPromoterStopPoint = sampleWithRequiredData;
        expectedResult = service.addPromoterStopPointToCollectionIfMissing([], promoterStopPoint);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(promoterStopPoint);
      });

      it('should not add a PromoterStopPoint to an array that contains it', () => {
        const promoterStopPoint: IPromoterStopPoint = sampleWithRequiredData;
        const promoterStopPointCollection: IPromoterStopPoint[] = [
          {
            ...promoterStopPoint,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPromoterStopPointToCollectionIfMissing(promoterStopPointCollection, promoterStopPoint);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PromoterStopPoint to an array that doesn't contain it", () => {
        const promoterStopPoint: IPromoterStopPoint = sampleWithRequiredData;
        const promoterStopPointCollection: IPromoterStopPoint[] = [sampleWithPartialData];
        expectedResult = service.addPromoterStopPointToCollectionIfMissing(promoterStopPointCollection, promoterStopPoint);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(promoterStopPoint);
      });

      it('should add only unique PromoterStopPoint to an array', () => {
        const promoterStopPointArray: IPromoterStopPoint[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const promoterStopPointCollection: IPromoterStopPoint[] = [sampleWithRequiredData];
        expectedResult = service.addPromoterStopPointToCollectionIfMissing(promoterStopPointCollection, ...promoterStopPointArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const promoterStopPoint: IPromoterStopPoint = sampleWithRequiredData;
        const promoterStopPoint2: IPromoterStopPoint = sampleWithPartialData;
        expectedResult = service.addPromoterStopPointToCollectionIfMissing([], promoterStopPoint, promoterStopPoint2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(promoterStopPoint);
        expect(expectedResult).toContain(promoterStopPoint2);
      });

      it('should accept null and undefined values', () => {
        const promoterStopPoint: IPromoterStopPoint = sampleWithRequiredData;
        expectedResult = service.addPromoterStopPointToCollectionIfMissing([], null, promoterStopPoint, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(promoterStopPoint);
      });

      it('should return initial array if no PromoterStopPoint is added', () => {
        const promoterStopPointCollection: IPromoterStopPoint[] = [sampleWithRequiredData];
        expectedResult = service.addPromoterStopPointToCollectionIfMissing(promoterStopPointCollection, undefined, null);
        expect(expectedResult).toEqual(promoterStopPointCollection);
      });
    });

    describe('comparePromoterStopPoint', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePromoterStopPoint(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePromoterStopPoint(entity1, entity2);
        const compareResult2 = service.comparePromoterStopPoint(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePromoterStopPoint(entity1, entity2);
        const compareResult2 = service.comparePromoterStopPoint(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePromoterStopPoint(entity1, entity2);
        const compareResult2 = service.comparePromoterStopPoint(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
