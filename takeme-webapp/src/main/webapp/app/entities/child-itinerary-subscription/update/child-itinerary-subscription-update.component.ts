import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ChildItinerarySubscriptionFormService, ChildItinerarySubscriptionFormGroup } from './child-itinerary-subscription-form.service';
import { IChildItinerarySubscription } from '../child-itinerary-subscription.model';
import { ChildItinerarySubscriptionService } from '../service/child-itinerary-subscription.service';
import { IChild } from 'app/entities/child/child.model';
import { ChildService } from 'app/entities/child/service/child.service';
import { IPromoterItinerary } from 'app/entities/promoter-itinerary/promoter-itinerary.model';
import { PromoterItineraryService } from 'app/entities/promoter-itinerary/service/promoter-itinerary.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { StatusType } from 'app/entities/enumerations/status-type.model';

@Component({
  selector: 'jhi-child-itinerary-subscription-update',
  templateUrl: './child-itinerary-subscription-update.component.html',
})
export class ChildItinerarySubscriptionUpdateComponent implements OnInit {
  isSaving = false;
  childItinerarySubscription: IChildItinerarySubscription | null = null;
  statusTypeValues = Object.keys(StatusType);

  childrenSharedCollection: IChild[] = [];
  promoterItinerariesSharedCollection: IPromoterItinerary[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: ChildItinerarySubscriptionFormGroup = this.childItinerarySubscriptionFormService.createChildItinerarySubscriptionFormGroup();

  constructor(
    protected childItinerarySubscriptionService: ChildItinerarySubscriptionService,
    protected childItinerarySubscriptionFormService: ChildItinerarySubscriptionFormService,
    protected childService: ChildService,
    protected promoterItineraryService: PromoterItineraryService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareChild = (o1: IChild | null, o2: IChild | null): boolean => this.childService.compareChild(o1, o2);

  comparePromoterItinerary = (o1: IPromoterItinerary | null, o2: IPromoterItinerary | null): boolean =>
    this.promoterItineraryService.comparePromoterItinerary(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ childItinerarySubscription }) => {
      this.childItinerarySubscription = childItinerarySubscription;
      if (childItinerarySubscription) {
        this.updateForm(childItinerarySubscription);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const childItinerarySubscription = this.childItinerarySubscriptionFormService.getChildItinerarySubscription(this.editForm);
    if (childItinerarySubscription.id !== null) {
      this.subscribeToSaveResponse(this.childItinerarySubscriptionService.update(childItinerarySubscription));
    } else {
      this.subscribeToSaveResponse(this.childItinerarySubscriptionService.create(childItinerarySubscription));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChildItinerarySubscription>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(childItinerarySubscription: IChildItinerarySubscription): void {
    this.childItinerarySubscription = childItinerarySubscription;
    this.childItinerarySubscriptionFormService.resetForm(this.editForm, childItinerarySubscription);

    this.childrenSharedCollection = this.childService.addChildToCollectionIfMissing<IChild>(
      this.childrenSharedCollection,
      childItinerarySubscription.child
    );
    this.promoterItinerariesSharedCollection = this.promoterItineraryService.addPromoterItineraryToCollectionIfMissing<IPromoterItinerary>(
      this.promoterItinerariesSharedCollection,
      childItinerarySubscription.promoterItinerary
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      childItinerarySubscription.user
    );
  }

  protected loadRelationshipsOptions(): void {
    this.childService
      .query()
      .pipe(map((res: HttpResponse<IChild[]>) => res.body ?? []))
      .pipe(
        map((children: IChild[]) =>
          this.childService.addChildToCollectionIfMissing<IChild>(children, this.childItinerarySubscription?.child)
        )
      )
      .subscribe((children: IChild[]) => (this.childrenSharedCollection = children));

    this.promoterItineraryService
      .query()
      .pipe(map((res: HttpResponse<IPromoterItinerary[]>) => res.body ?? []))
      .pipe(
        map((promoterItineraries: IPromoterItinerary[]) =>
          this.promoterItineraryService.addPromoterItineraryToCollectionIfMissing<IPromoterItinerary>(
            promoterItineraries,
            this.childItinerarySubscription?.promoterItinerary
          )
        )
      )
      .subscribe((promoterItineraries: IPromoterItinerary[]) => (this.promoterItinerariesSharedCollection = promoterItineraries));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.childItinerarySubscription?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
