import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import {isPresent} from 'app/core/util/operators';
import {DATE_FORMAT} from 'app/config/input.constants';
import {ApplicationConfigService} from 'app/core/config/application-config.service';
import {createRequestOption} from 'app/core/request/request-util';
import {IPromoterService, NewPromoterService} from '../promoter-service.model';

export type PartialUpdatePromoterService = Partial<IPromoterService> & Pick<IPromoterService, 'id'>;

type RestOf<T extends IPromoterService | NewPromoterService> = Omit<T, 'startDate' | 'endDate'> & {
    startDate?: string | null;
    endDate?: string | null;
};

export type RestPromoterService = RestOf<IPromoterService>;
export type EntityResponseType = HttpResponse<IPromoterService>;
export type EntityArrayResponseType = HttpResponse<IPromoterService[]>;

@Injectable({providedIn: 'root'})
export class PromoterServiceService {
    protected resourceUrl = this.applicationConfigService.getEndpointFor('api/promoter-services');

    constructor(
        private http: HttpClient,
        private applicationConfigService: ApplicationConfigService
    ) {
    }

    partialUpdate(promoterService: PartialUpdatePromoterService): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(promoterService);
        return this.http
            .patch<RestPromoterService>(`${this.resourceUrl}/${this.getPromoterServiceIdentifier(promoterService)}`, copy, {
                observe: 'response',
            })
            .pipe(map(res => this.convertResponseFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<RestPromoterService[]>(this.resourceUrl, {params: options, observe: 'response'})
            .pipe(map(res => this.convertResponseArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<{}>> {
        return this.http.delete(`${this.resourceUrl}/${id}`, {observe: 'response'});
    }


    comparePromoterService(o1: Pick<IPromoterService, 'id'> | null, o2: Pick<IPromoterService, 'id'> | null): boolean {
        return o1 && o2 ? this.getPromoterServiceIdentifier(o1) === this.getPromoterServiceIdentifier(o2) : o1 === o2;
    }

    addPromoterServiceToCollectionIfMissing<Type extends Pick<IPromoterService, 'id'>>(
        promoterServiceCollection: Type[],
        ...promoterServicesToCheck: (Type | null | undefined)[]
    ): Type[] {
        const promoterServices: Type[] = promoterServicesToCheck.filter(isPresent);
        if (promoterServices.length > 0) {
            const promoterServiceCollectionIdentifiers = promoterServiceCollection.map(
                promoterServiceItem => this.getPromoterServiceIdentifier(promoterServiceItem)!
            );
            const promoterServicesToAdd = promoterServices.filter(promoterServiceItem => {
                const promoterServiceIdentifier = this.getPromoterServiceIdentifier(promoterServiceItem);
                if (promoterServiceCollectionIdentifiers.includes(promoterServiceIdentifier)) {
                    return false;
                }
                promoterServiceCollectionIdentifiers.push(promoterServiceIdentifier);
                return true;
            });
            return [...promoterServicesToAdd, ...promoterServiceCollection];
        }
        return promoterServiceCollection;
    }

    protected convertResponseArrayFromServer(res: HttpResponse<RestPromoterService[]>): HttpResponse<IPromoterService[]> {
        return res.clone({
            body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
        });
    }

    public getPromoterServiceIdentifier(promoterService: Pick<IPromoterService, 'id'>): number {
        return promoterService.id;
    }

    public getPromoterServices(): Observable<HttpResponse<IPromoterService[]>> {
        return this.http.get<IPromoterService[]>(`api/promoter-services`, {observe: 'response'});
    }

    public getPromoterServiceByPromoterServiceId(promoterServiceId): Observable<HttpResponse<IPromoterService>> {
        return this.http
            .get<RestOf<IPromoterService>>(`api/promoter-services/${promoterServiceId}`, {observe: 'response'})
            .pipe(map(res => this.convertResponseFromServer(res)));
    }

    public updatePromoterServiceByPromoterServiceId(promoterServiceId, promoterService): Observable<HttpResponse<IPromoterService>> {
        return this.http
            .put<RestOf<IPromoterService>>(`api/promoter-services/${promoterServiceId}`, this.convertDateFromClient(promoterService), {observe: 'response'})
            .pipe(map(res => this.convertResponseFromServer(res)));
    }

    public createPromoterService(promoterService: IPromoterService | (Omit<IPromoterService, "id"> & { id: null })): Observable<HttpResponse<IPromoterService>> {
        return this.http
            .post<RestOf<IPromoterService>>(`api/promoter-services`, this.convertDateFromClient(promoterService), {observe: 'response'})
            .pipe(map(res => this.convertResponseFromServer(res)));
    }

    private convertResponseFromServer(res: HttpResponse<RestPromoterService>): HttpResponse<IPromoterService> {
        return res.clone({
            body: res.body ? this.convertDateFromServer(res.body) : null,
        });
    }

    private convertDateFromServer(restPromoterService: RestPromoterService): IPromoterService {
        return {
            ...restPromoterService,
            startDate: restPromoterService.startDate ? dayjs(restPromoterService.startDate) : undefined,
            endDate: restPromoterService.endDate ? dayjs(restPromoterService.endDate) : undefined,
        };
    }

    private convertDateFromClient<T extends IPromoterService | NewPromoterService | PartialUpdatePromoterService>(promoterService: T): RestOf<T> {
        return {
            ...promoterService,
            startDate: promoterService.startDate?.format(DATE_FORMAT) ?? null,
            endDate: promoterService.endDate?.format(DATE_FORMAT) ?? null,
        };
    }
}
