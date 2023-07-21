import {Injectable} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {Resolve, ActivatedRouteSnapshot, Router} from '@angular/router';
import {Observable, of, EMPTY} from 'rxjs';
import {mergeMap} from 'rxjs/operators';
import {IPromoterService} from '../promoter-service.model';
import {PromoterServiceService} from '../service/promoter-service.service';

@Injectable({providedIn: 'root'})
export class PromoterServiceRoutingResolveService implements Resolve<IPromoterService | null> {
    constructor(
        private service: PromoterServiceService,
        private router: Router
    ) {
    }

    resolve(route: ActivatedRouteSnapshot): Observable<IPromoterService | null | never> {
        const id = route.params['id'];
        if (id) {
            return this.service
                .getPromoterServiceByPromoterServiceId(id)
                .pipe(
                    mergeMap((promoterService: HttpResponse<IPromoterService>) => {
                        if (promoterService.body) {
                            return of(promoterService.body);
                        } else {
                            this.router.navigate(['404']).then();
                            return EMPTY;
                        }
                    })
                );
        }
        return of(null);
    }
}
