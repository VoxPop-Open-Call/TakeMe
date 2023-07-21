import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IChildItinerarySubscription } from '../child-itinerary-subscription.model';
import { ChildItinerarySubscriptionService } from '../service/child-itinerary-subscription.service';

import { ChildItinerarySubscriptionRoutingResolveService } from './child-itinerary-subscription-routing-resolve.service';

describe('ChildItinerarySubscription routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ChildItinerarySubscriptionRoutingResolveService;
  let service: ChildItinerarySubscriptionService;
  let resultChildItinerarySubscription: IChildItinerarySubscription | null | undefined;

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
    routingResolveService = TestBed.inject(ChildItinerarySubscriptionRoutingResolveService);
    service = TestBed.inject(ChildItinerarySubscriptionService);
    resultChildItinerarySubscription = undefined;
  });

  describe('resolve', () => {
    it('should return IChildItinerarySubscription returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultChildItinerarySubscription = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultChildItinerarySubscription).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultChildItinerarySubscription = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultChildItinerarySubscription).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IChildItinerarySubscription>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultChildItinerarySubscription = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultChildItinerarySubscription).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
