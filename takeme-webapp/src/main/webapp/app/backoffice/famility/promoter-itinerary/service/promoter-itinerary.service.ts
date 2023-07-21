import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import {isPresent} from 'app/core/util/operators';
import {DATE_FORMAT} from 'app/config/input.constants';
import {ApplicationConfigService} from 'app/core/config/application-config.service';
import {createRequestOption} from 'app/core/request/request-util';
import {IPromoterItinerary, NewPromoterItinerary} from '../promoter-itinerary.model';
import {ChildStopPointsServices} from "../../../../shared/model/child-stop-points-services.model";
import {ServiceStopPoint} from "../../../../shared/model/service-stop-point.model";
import moment from "moment/moment";

export type PartialUpdatePromoterItinerary = Partial<IPromoterItinerary> & Pick<IPromoterItinerary, 'id'>;

type RestOf<T extends IPromoterItinerary | NewPromoterItinerary> = Omit<T, 'startDate' | 'endDate'> & {
    startDate?: string | null;
    endDate?: string | null;
};

export type RestPromoterItinerary = RestOf<IPromoterItinerary>;
export type EntityResponseType = HttpResponse<IPromoterItinerary>;
export type EntityArrayResponseType = HttpResponse<IPromoterItinerary[]>;

@Injectable({providedIn: 'root'})
export class PromoterItineraryService {
    protected resourceUrl = this.applicationConfigService.getEndpointFor('api/promoter-itineraries');

    constructor(
        protected http: HttpClient,
        protected applicationConfigService: ApplicationConfigService
    ) {
    }

    create(promoterItinerary: IPromoterItinerary | (Omit<IPromoterItinerary, "id"> & {
        id: null
    })): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(promoterItinerary);
        return this.http
            .post<RestPromoterItinerary>(this.resourceUrl, copy, {observe: 'response'})
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
            .get<RestPromoterItinerary>(`${this.resourceUrl}/${id}`, {observe: 'response'})
            .pipe(map(res => this.convertResponseFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<RestPromoterItinerary[]>(this.resourceUrl, {params: options, observe: 'response'})
            .pipe(map(res => this.convertResponseArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<{}>> {
        return this.http.delete(`${this.resourceUrl}/${id}`, {observe: 'response'});
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
        };
    }

    protected convertDateFromServer(restPromoterItinerary: RestPromoterItinerary): IPromoterItinerary {
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

    public getPromoterItineraries(request): Observable<HttpResponse<IPromoterItinerary[]>> {
        let params: HttpParams = new HttpParams();
        if (request.serviceId) params = params.set('serviceId.equals', request.serviceId);
        if (request.operatorId) params = params.set('organizationId.equals', request.operatorId);
        if (request.active && request.active == true) params = params.set('endDate.specified', 'false');
        if (request.endDateHasNotPassed && request.endDateHasNotPassed == true) params = params.set('endDate.greaterThanOrEqual', this.getStringifiedTodayDate());
        return this.http.get<IPromoterItinerary[]>(`api/promoter-itineraries`, {params, observe: 'response'});
    }

    private getStringifiedTodayDate(): string {
      const todayDate = new Date();
      const year = todayDate.getFullYear();
      const monthWithPadStart = (todayDate.getMonth() + 1).toString().padStart(2, '0');
      const dayWithPadStart = todayDate.getDate().toString().padStart(2, '0');
      return `${year}-${monthWithPadStart}-${dayWithPadStart}`;
    }

    public getPromoterItineraryServiceStopPoints(id): Observable<HttpResponse<ChildStopPointsServices[]>> {
        return this.http
            .get<ChildStopPointsServices[]>(`api/promoter-itineraries/${id}/service-stop-points`, {observe: 'response'})
            .pipe(map((res: HttpResponse<ChildStopPointsServices[]>) => this.convertDateArrayFromServerOfStopsAvailableForItinerary(res)));
    }

    private convertDateArrayFromServerOfStopsAvailableForItinerary(res: HttpResponse<ChildStopPointsServices[]>): HttpResponse<ChildStopPointsServices[]> {
        if (res.body) {
            res.body.forEach((childStopPointService: ChildStopPointsServices) => {
                childStopPointService.serviceStopPoints.forEach((serviceStopPoint: ServiceStopPoint) => {
                    serviceStopPoint.startFrequencyDate = serviceStopPoint.startFrequencyDate != null ? moment(serviceStopPoint.startFrequencyDate) : null;
                });
            });
        }
        return res;
    }
}
