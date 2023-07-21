import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPromoterStopPoint } from '../promoter-stop-point.model';
import { PromoterStopPointService } from '../service/promoter-stop-point.service';

@Injectable({ providedIn: 'root' })
export class PromoterStopPointRoutingResolveService implements Resolve<IPromoterStopPoint | null> {
  constructor(protected service: PromoterStopPointService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPromoterStopPoint | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((promoterStopPoint: HttpResponse<IPromoterStopPoint>) => {
          if (promoterStopPoint.body) {
            return of(promoterStopPoint.body);
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
