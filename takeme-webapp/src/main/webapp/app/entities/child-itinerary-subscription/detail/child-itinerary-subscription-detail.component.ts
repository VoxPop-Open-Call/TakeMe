import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChildItinerarySubscription } from '../child-itinerary-subscription.model';

@Component({
  selector: 'jhi-child-itinerary-subscription-detail',
  templateUrl: './child-itinerary-subscription-detail.component.html',
})
export class ChildItinerarySubscriptionDetailComponent implements OnInit {
  childItinerarySubscription: IChildItinerarySubscription | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ childItinerarySubscription }) => {
      this.childItinerarySubscription = childItinerarySubscription;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
