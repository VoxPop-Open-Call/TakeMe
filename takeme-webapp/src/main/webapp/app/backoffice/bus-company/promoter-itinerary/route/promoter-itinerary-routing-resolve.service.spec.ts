import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPromoterItinerary } from '../promoter-itinerary.model';
import { OperatorPromoterItineraryService } from '../service/promoter-itinerary.service';

import { OperatorPromoterItineraryRoutingResolveService } from './promoter-itinerary-routing-resolve.service';

describe('PromoterItinerary routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: OperatorPromoterItineraryRoutingResolveService;
  let service: OperatorPromoterItineraryService;
  let resultPromoterItinerary: IPromoterItinerary | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(OperatorPromoterItineraryRoutingResolveService);
    service = TestBed.inject(OperatorPromoterItineraryService);
    resultPromoterItinerary = undefined;
  });

  describe('resolve', () => {
    it('should return IPromoterItinerary returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPromoterItinerary = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPromoterItinerary).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPromoterItinerary = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPromoterItinerary).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPromoterItinerary>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPromoterItinerary = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPromoterItinerary).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
