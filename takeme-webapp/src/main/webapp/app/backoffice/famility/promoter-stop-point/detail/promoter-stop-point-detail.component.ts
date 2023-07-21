import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPromoterStopPoint } from '../promoter-stop-point.model';
import { PromoterItineraryService } from "../../promoter-itinerary/service/promoter-itinerary.service";
import { IPromoterItinerary } from "../../promoter-itinerary/promoter-itinerary.model";

@Component({
  selector: 'jhi-promoter-stop-point-detail',
  templateUrl: './promoter-stop-point-detail.component.html',
})
export class PromoterStopPointDetailComponent implements OnInit {
  promoterStopPoint: IPromoterStopPoint | null = null;
  promoterItinerary: IPromoterItinerary | null = null;

  constructor(protected activatedRoute: ActivatedRoute,
              protected promoterItineraryService: PromoterItineraryService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ promoterStopPoint }) => {
      this.promoterStopPoint = this.convertDateFromClient(promoterStopPoint);
    });

    this.promoterItineraryService.find(this.promoterStopPoint.promoterItineraryId).subscribe(res => {
      this.promoterItinerary = res.body;
    });
  }

  previousState(): void {
    window.history.back();
  }
  protected convertDateFromClient(data) {
    return Object.assign({}, data, {
      scheduledTime: data.scheduledTime != null && data.scheduledTime.isValid() ? data.scheduledTime.toJSON() : null
    });
  }
}
