import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPromoterStopPoint, NewPromoterStopPoint } from '../promoter-stop-point.model';

export type PartialUpdatePromoterStopPoint = Partial<IPromoterStopPoint> & Pick<IPromoterStopPoint, 'id'>;

type RestOf<T extends IPromoterStopPoint | NewPromoterStopPoint> = Omit<T, 'scheduledTime'> & {
  scheduledTime?: string | null;
};

export type RestPromoterStopPoint = RestOf<IPromoterStopPoint>;

export type NewRestPromoterStopPoint = RestOf<NewPromoterStopPoint>;

export type PartialUpdateRestPromoterStopPoint = RestOf<PartialUpdatePromoterStopPoint>;

export type EntityResponseType = HttpResponse<IPromoterStopPoint>;
export type EntityArrayResponseType = HttpResponse<IPromoterStopPoint[]>;

@Injectable({ providedIn: 'root' })
export class PromoterStopPointService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/promoter-stop-points');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(promoterStopPoint: IPromoterStopPoint | (Omit<IPromoterStopPoint, "id"> & { id: null })): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promoterStopPoint);
    return this.http
      .post<RestPromoterStopPoint>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(promoterStopPoint: IPromoterStopPoint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promoterStopPoint);
    return this.http
      .put<RestPromoterStopPoint>(`${this.resourceUrl}/${this.getPromoterStopPointIdentifier(promoterStopPoint)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(promoterStopPoint: PartialUpdatePromoterStopPoint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promoterStopPoint);
    return this.http
      .patch<RestPromoterStopPoint>(`${this.resourceUrl}/${this.getPromoterStopPointIdentifier(promoterStopPoint)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPromoterStopPoint>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPromoterStopPoint[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPromoterStopPointIdentifier(promoterStopPoint: Pick<IPromoterStopPoint, 'id'>): number {
    return promoterStopPoint.id;
  }

  comparePromoterStopPoint(o1: Pick<IPromoterStopPoint, 'id'> | null, o2: Pick<IPromoterStopPoint, 'id'> | null): boolean {
    return o1 && o2 ? this.getPromoterStopPointIdentifier(o1) === this.getPromoterStopPointIdentifier(o2) : o1 === o2;
  }

  addPromoterStopPointToCollectionIfMissing<Type extends Pick<IPromoterStopPoint, 'id'>>(
    promoterStopPointCollection: Type[],
    ...promoterStopPointsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const promoterStopPoints: Type[] = promoterStopPointsToCheck.filter(isPresent);
    if (promoterStopPoints.length > 0) {
      const promoterStopPointCollectionIdentifiers = promoterStopPointCollection.map(
        promoterStopPointItem => this.getPromoterStopPointIdentifier(promoterStopPointItem)!
      );
      const promoterStopPointsToAdd = promoterStopPoints.filter(promoterStopPointItem => {
        const promoterStopPointIdentifier = this.getPromoterStopPointIdentifier(promoterStopPointItem);
        if (promoterStopPointCollectionIdentifiers.includes(promoterStopPointIdentifier)) {
          return false;
        }
        promoterStopPointCollectionIdentifiers.push(promoterStopPointIdentifier);
        return true;
      });
      return [...promoterStopPointsToAdd, ...promoterStopPointCollection];
    }
    return promoterStopPointCollection;
  }

  getPromoterStopPoints(id: number): Observable<HttpResponse<IPromoterStopPoint[]>> {
    return this.http
      .get<RestPromoterStopPoint[]>(`${this.resourceUrl}/promoter-itinerary/${id}`, {observe: 'response'})
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  protected convertDateFromClient<T extends IPromoterStopPoint | NewPromoterStopPoint | PartialUpdatePromoterStopPoint>(
    promoterStopPoint: T
  ): RestOf<T> {
    return {
      ...promoterStopPoint,
      scheduledTime: promoterStopPoint.scheduledTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPromoterStopPoint: RestPromoterStopPoint): IPromoterStopPoint {
    return {
      ...restPromoterStopPoint,
      scheduledTime: restPromoterStopPoint.scheduledTime ? dayjs(restPromoterStopPoint.scheduledTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPromoterStopPoint>): HttpResponse<IPromoterStopPoint> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPromoterStopPoint[]>): HttpResponse<IPromoterStopPoint[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
