import { Injectable } from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IPromoterItinerary, NewPromoterItinerary } from '../promoter-itinerary.model';
import { IPromoterStopPoint } from "../../../famility/promoter-stop-point/promoter-stop-point.model";

export type PartialUpdatePromoterItinerary = Partial<IPromoterItinerary> & Pick<IPromoterItinerary, 'id'>;
type RestOf<T extends IPromoterItinerary | NewPromoterItinerary> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
  promoterItineraryStopPoints?: IPromoterStopPoint[] | null;
};

export type RestPromoterItinerary = RestOf<IPromoterItinerary>;
export type NewRestPromoterItinerary = RestOf<NewPromoterItinerary>;
export type PartialUpdateRestPromoterItinerary = RestOf<PartialUpdatePromoterItinerary>;
export type EntityResponseType = HttpResponse<IPromoterItinerary>;
export type EntityArrayResponseType = HttpResponse<IPromoterItinerary[]>;

@Injectable({ providedIn: 'root' })
export class OperatorPromoterItineraryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/promoter-itineraries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(promoterItinerary: IPromoterItinerary | (Omit<IPromoterItinerary, "id"> & { id: null })): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promoterItinerary);
    return this.http
      .post<RestPromoterItinerary>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(promoterItinerary: IPromoterItinerary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promoterItinerary);
    return this.http
      .put<RestPromoterItinerary>(`${this.resourceUrl}/${this.getPromoterItineraryIdentifier(promoterItinerary)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(promoterItinerary: PartialUpdatePromoterItinerary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(promoterItinerary);
    return this.http
      .patch<RestPromoterItinerary>(`${this.resourceUrl}/${this.getPromoterItineraryIdentifier(promoterItinerary)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPromoterItinerary>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

    query(operatorId, pagination): Observable<EntityArrayResponseType> {
        let params: HttpParams = new HttpParams();
        params = params.set('id', operatorId);
        params = params.set('page', String(pagination.page));
        params = params.set('size', String(pagination.itemsPerPage));
        params = params.set('sort', `${pagination.sortProperty},${pagination.sortDirection}`);
        return this.http
            .get<RestPromoterItinerary[]>(`${this.resourceUrl}/by-organization`, {params, observe: 'response'})
            .pipe(map(res => this.convertResponseArrayFromServer(res)));
    }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPromoterItineraryIdentifier(promoterItinerary: Pick<IPromoterItinerary, 'id'>): number {
    return promoterItinerary.id;
  }

  comparePromoterItinerary(o1: Pick<IPromoterItinerary, 'id'> | null, o2: Pick<IPromoterItinerary, 'id'> | null): boolean {
    return o1 && o2 ? this.getPromoterItineraryIdentifier(o1) === this.getPromoterItineraryIdentifier(o2) : o1 === o2;
  }

  addPromoterItineraryToCollectionIfMissing<Type extends Pick<IPromoterItinerary, 'id'>>(
    promoterItineraryCollection: Type[],
    ...promoterItinerariesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const promoterItineraries: Type[] = promoterItinerariesToCheck.filter(isPresent);
    if (promoterItineraries.length > 0) {
      const promoterItineraryCollectionIdentifiers = promoterItineraryCollection.map(
        promoterItineraryItem => this.getPromoterItineraryIdentifier(promoterItineraryItem)!
      );
      const promoterItinerariesToAdd = promoterItineraries.filter(promoterItineraryItem => {
        const promoterItineraryIdentifier = this.getPromoterItineraryIdentifier(promoterItineraryItem);
        if (promoterItineraryCollectionIdentifiers.includes(promoterItineraryIdentifier)) {
          return false;
        }
        promoterItineraryCollectionIdentifiers.push(promoterItineraryIdentifier);
        return true;
      });
      return [...promoterItinerariesToAdd, ...promoterItineraryCollection];
    }
    return promoterItineraryCollection;
  }

  protected convertDateFromClient<T extends IPromoterItinerary | NewPromoterItinerary | PartialUpdatePromoterItinerary>(
    promoterItinerary: T
  ): RestOf<T> {
    return {
      ...promoterItinerary,
      startDate: promoterItinerary.startDate?.format(DATE_FORMAT) ?? null,
      endDate: promoterItinerary.endDate?.format(DATE_FORMAT) ?? null,
      promoterItineraryStopPoints: this.convertDateWithStopPointsFromClient(promoterItinerary) ?? null,
    };
  }

  convertDateFromServer(restPromoterItinerary: RestPromoterItinerary): IPromoterItinerary {
    return {
      ...restPromoterItinerary,
      startDate: restPromoterItinerary.startDate ? dayjs(restPromoterItinerary.startDate) : undefined,
      endDate: restPromoterItinerary.endDate ? dayjs(restPromoterItinerary.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPromoterItinerary>): HttpResponse<IPromoterItinerary> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPromoterItinerary[]>): HttpResponse<IPromoterItinerary[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
  protected convertDateWithStopPointsFromClient(itinerary) {
    const itineraryStopPointList = [];

    if(itinerary.promoterItineraryStopPoints){
      itinerary.promoterItineraryStopPoints.forEach(stopPoint => {
        stopPoint.scheduledTime = stopPoint.scheduledTime ? dayjs(stopPoint.scheduledTime).format('YYYY-MM-DDTHH:mm:00') + "Z" : undefined;

        itineraryStopPointList.push(stopPoint);
      })
    }

    return itineraryStopPointList
  }
}
