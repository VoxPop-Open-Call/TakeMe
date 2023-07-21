import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PromoterStopPointFormService, PromoterStopPointFormGroup } from './promoter-stop-point-form.service';
import { IPromoterStopPoint } from '../promoter-stop-point.model';
import { PromoterStopPointService } from '../service/promoter-stop-point.service';
import { IPromoterItinerary } from 'app/backoffice/famility/promoter-itinerary/promoter-itinerary.model';
import { PromoterItineraryService } from 'app/backoffice/famility/promoter-itinerary/service/promoter-itinerary.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';

@Component({
  selector: 'jhi-promoter-stop-point-update',
  templateUrl: './promoter-stop-point-update.component.html',
})
export class PromoterStopPointUpdateComponent implements OnInit {
  isSaving = false;
  promoterStopPoint: IPromoterStopPoint | null = null;

  promoterItinerariesSharedCollection: IPromoterItinerary[] = [];
  locationsSharedCollection: ILocation[] = [];

  editForm: PromoterStopPointFormGroup = this.promoterStopPointFormService.createPromoterStopPointFormGroup();

  constructor(
    protected promoterStopPointService: PromoterStopPointService,
    protected promoterStopPointFormService: PromoterStopPointFormService,
    protected promoterItineraryService: PromoterItineraryService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePromoterItinerary = (o1: IPromoterItinerary | null, o2: IPromoterItinerary | null): boolean =>
    this.promoterItineraryService.comparePromoterItinerary(o1, o2);

  compareLocation = (o1: ILocation | null, o2: ILocation | null): boolean => this.locationService.compareLocation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ promoterStopPoint }) => {
      this.promoterStopPoint = promoterStopPoint;
      if (promoterStopPoint) {
        this.updateForm(promoterStopPoint);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const promoterStopPoint = this.promoterStopPointFormService.getPromoterStopPoint(this.editForm);
    if (promoterStopPoint.id !== null) {
      this.subscribeToSaveResponse(this.promoterStopPointService.update(promoterStopPoint));
    } else {
      this.subscribeToSaveResponse(this.promoterStopPointService.create(promoterStopPoint));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPromoterStopPoint>>): void {
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

  protected updateForm(promoterStopPoint: IPromoterStopPoint): void {
    this.promoterStopPoint = promoterStopPoint;
    this.promoterStopPointFormService.resetForm(this.editForm, promoterStopPoint);

    let promotorItinerary;
    this.promoterItineraryService.find(promoterStopPoint.promoterItineraryId).subscribe(res => {
      promotorItinerary = res.body;
    })

    this.promoterItinerariesSharedCollection = this.promoterItineraryService.addPromoterItineraryToCollectionIfMissing<IPromoterItinerary>(
      this.promoterItinerariesSharedCollection,
      promotorItinerary
    );
    this.locationsSharedCollection = this.locationService.addLocationToCollectionIfMissing<ILocation>(
      this.locationsSharedCollection,
      promoterStopPoint.location
    );
  }

  protected loadRelationshipsOptions(): void {
    let promotorItinerary;
    this.promoterItineraryService.find(this.promoterStopPoint?.promoterItineraryId).subscribe(res => {
      promotorItinerary = res.body;
    })

    this.promoterItineraryService
      .query()
      .pipe(map((res: HttpResponse<IPromoterItinerary[]>) => res.body ?? []))
      .pipe(
        map((promoterItineraries: IPromoterItinerary[]) =>
          this.promoterItineraryService.addPromoterItineraryToCollectionIfMissing<IPromoterItinerary>(
            promoterItineraries,
            promotorItinerary
          )
        )
      )
      .subscribe((promoterItineraries: IPromoterItinerary[]) => (this.promoterItinerariesSharedCollection = promoterItineraries));

    this.locationService
      .query()
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) =>
          this.locationService.addLocationToCollectionIfMissing<ILocation>(locations, this.promoterStopPoint?.location)
        )
      )
      .subscribe((locations: ILocation[]) => (this.locationsSharedCollection = locations));
  }
}
