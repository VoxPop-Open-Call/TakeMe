import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChildItinerarySubscription, NewChildItinerarySubscription } from '../child-itinerary-subscription.model';

export type PartialUpdateChildItinerarySubscription = Partial<IChildItinerarySubscription> & Pick<IChildItinerarySubscription, 'id'>;

type RestOf<T extends IChildItinerarySubscription | NewChildItinerarySubscription> = Omit<
  T,
  'subscriptionDate' | 'activationDate' | 'deactivationDate'
> & {
  subscriptionDate?: string | null;
  activationDate?: string | null;
  deactivationDate?: string | null;
};

export type RestChildItinerarySubscription = RestOf<IChildItinerarySubscription>;

export type NewRestChildItinerarySubscription = RestOf<NewChildItinerarySubscription>;

export type PartialUpdateRestChildItinerarySubscription = RestOf<PartialUpdateChildItinerarySubscription>;

export type EntityResponseType = HttpResponse<IChildItinerarySubscription>;
export type EntityArrayResponseType = HttpResponse<IChildItinerarySubscription[]>;

@Injectable({ providedIn: 'root' })
export class ChildItinerarySubscriptionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/child-itinerary-subscriptions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(childItinerarySubscription: NewChildItinerarySubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(childItinerarySubscription);
    return this.http
      .post<RestChildItinerarySubscription>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(childItinerarySubscription: IChildItinerarySubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(childItinerarySubscription);
    return this.http
      .put<RestChildItinerarySubscription>(
        `${this.resourceUrl}/${this.getChildItinerarySubscriptionIdentifier(childItinerarySubscription)}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(childItinerarySubscription: PartialUpdateChildItinerarySubscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(childItinerarySubscription);
    return this.http
      .patch<RestChildItinerarySubscription>(
        `${this.resourceUrl}/${this.getChildItinerarySubscriptionIdentifier(childItinerarySubscription)}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestChildItinerarySubscription>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestChildItinerarySubscription[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getChildItinerarySubscriptionIdentifier(childItinerarySubscription: Pick<IChildItinerarySubscription, 'id'>): number {
    return childItinerarySubscription.id;
  }

  compareChildItinerarySubscription(
    o1: Pick<IChildItinerarySubscription, 'id'> | null,
    o2: Pick<IChildItinerarySubscription, 'id'> | null
  ): boolean {
    return o1 && o2 ? this.getChildItinerarySubscriptionIdentifier(o1) === this.getChildItinerarySubscriptionIdentifier(o2) : o1 === o2;
  }

  addChildItinerarySubscriptionToCollectionIfMissing<Type extends Pick<IChildItinerarySubscription, 'id'>>(
    childItinerarySubscriptionCollection: Type[],
    ...childItinerarySubscriptionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const childItinerarySubscriptions: Type[] = childItinerarySubscriptionsToCheck.filter(isPresent);
    if (childItinerarySubscriptions.length > 0) {
      const childItinerarySubscriptionCollectionIdentifiers = childItinerarySubscriptionCollection.map(
        childItinerarySubscriptionItem => this.getChildItinerarySubscriptionIdentifier(childItinerarySubscriptionItem)!
      );
      const childItinerarySubscriptionsToAdd = childItinerarySubscriptions.filter(childItinerarySubscriptionItem => {
        const childItinerarySubscriptionIdentifier = this.getChildItinerarySubscriptionIdentifier(childItinerarySubscriptionItem);
        if (childItinerarySubscriptionCollectionIdentifiers.includes(childItinerarySubscriptionIdentifier)) {
          return false;
        }
        childItinerarySubscriptionCollectionIdentifiers.push(childItinerarySubscriptionIdentifier);
        return true;
      });
      return [...childItinerarySubscriptionsToAdd, ...childItinerarySubscriptionCollection];
    }
    return childItinerarySubscriptionCollection;
  }

  protected convertDateFromClient<
    T extends IChildItinerarySubscription | NewChildItinerarySubscription | PartialUpdateChildItinerarySubscription
  >(childItinerarySubscription: T): RestOf<T> {
    return {
      ...childItinerarySubscription,
      subscriptionDate: childItinerarySubscription.subscriptionDate?.toJSON() ?? null,
      activationDate: childItinerarySubscription.activationDate?.toJSON() ?? null,
      deactivationDate: childItinerarySubscription.deactivationDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restChildItinerarySubscription: RestChildItinerarySubscription): IChildItinerarySubscription {
    return {
      ...restChildItinerarySubscription,
      subscriptionDate: restChildItinerarySubscription.subscriptionDate
        ? dayjs(restChildItinerarySubscription.subscriptionDate)
        : undefined,
      activationDate: restChildItinerarySubscription.activationDate ? dayjs(restChildItinerarySubscription.activationDate) : undefined,
      deactivationDate: restChildItinerarySubscription.deactivationDate
        ? dayjs(restChildItinerarySubscription.deactivationDate)
        : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestChildItinerarySubscription>): HttpResponse<IChildItinerarySubscription> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(
    res: HttpResponse<RestChildItinerarySubscription[]>
  ): HttpResponse<IChildItinerarySubscription[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
