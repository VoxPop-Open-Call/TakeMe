import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChildItinerarySubscription } from '../child-itinerary-subscription.model';
import { ChildItinerarySubscriptionService } from '../service/child-itinerary-subscription.service';

@Injectable({ providedIn: 'root' })
export class ChildItinerarySubscriptionRoutingResolveService implements Resolve<IChildItinerarySubscription | null> {
  constructor(protected service: ChildItinerarySubscriptionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChildItinerarySubscription | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((childItinerarySubscription: HttpResponse<IChildItinerarySubscription>) => {
          if (childItinerarySubscription.body) {
            return of(childItinerarySubscription.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
