import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPromoterItinerary } from '../promoter-itinerary.model';
import { OperatorPromoterItineraryService } from "../service/promoter-itinerary.service";

@Injectable({ providedIn: 'root' })
export class OperatorPromoterItineraryRoutingResolveService implements Resolve<IPromoterItinerary | null> {
  constructor(protected service: OperatorPromoterItineraryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPromoterItinerary | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((promoterItinerary: HttpResponse<IPromoterItinerary>) => {
          if (promoterItinerary.body) {
            return of(promoterItinerary.body);
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
