import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PromoterItineraryFormService, PromoterItineraryFormGroup } from './promoter-itinerary-form.service';
import { IPromoterItinerary } from '../promoter-itinerary.model';
import { IPromoterService } from 'app/backoffice/famility/promoter-service/promoter-service.model';
import { PromoterServiceService } from 'app/backoffice/famility/promoter-service/service/promoter-service.service';
import { CdkDragDrop } from "@angular/cdk/drag-drop";
import { ILocation } from "../../../../shared/model/location.model";
import { BuildAddress } from "../../../../shared/util/build-address";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { PromoterStopPointSelectDialogComponent } from "../stop-point/promoter-stop-point-select-dialog.component";
import { OrganizationService } from "../../../../entities/organization/organization.service";
import { Principal } from "../../../../core";
import { IPromoterStopPoint } from "../../../famility/promoter-stop-point/promoter-stop-point.model";
import { PromoterStopPointService } from "../../../famility/promoter-stop-point/service/promoter-stop-point.service";
import { ChildItinerarySubscriptionService } from "../../../../entities/child-itinerary-subscription/service/child-itinerary-subscription.service";
import { LocationService } from 'app/entities/location/service/location.service';
import { OperatorPromoterItineraryService } from "../service/promoter-itinerary.service";

@Component({
  selector: 'jhi-promoter-itinerary-update',
  templateUrl: './promoter-itinerary-update.component.html',
})
export class PromoterItineraryUpdateComponent implements OnInit {
  isSaving = false;
  promoterItinerary: IPromoterItinerary | null = null;

  promoterServicesSharedCollection: IPromoterService[] = [];
  promoterItineraryStopPoints: IPromoterStopPoint[] = [];
  promoterItineraryStopPointsToDelete: number[] = [];

  editForm: PromoterItineraryFormGroup = this.promoterItineraryFormService.createPromoterItineraryFormGroup();

  constructor(
    protected promoterItineraryService: OperatorPromoterItineraryService,
    protected promoterItineraryFormService: PromoterItineraryFormService,
    protected promoterServiceService: PromoterServiceService,
    protected organizationService: OrganizationService,
    protected promoterStopPointService: PromoterStopPointService,
    protected childItinerarySubscriptionService: ChildItinerarySubscriptionService,
    protected locationService: LocationService,
    private modalService: NgbModal,
    private router: Router,
    protected activatedRoute: ActivatedRoute,
    protected principal: Principal
  ) {}

  comparePromoterService = (o1: IPromoterService | null, o2: IPromoterService | null): boolean =>
    this.promoterServiceService.comparePromoterService(o1, o2);

  ngOnInit(): void {
    this.organizationService.find(+this.principal.getIdOrganization()).subscribe(res => {
      this.editForm.controls.organization.patchValue(res.body);
    })

    this.activatedRoute.data.subscribe(({ promoterItinerary }) => {
      this.promoterItinerary = promoterItinerary;
      if (promoterItinerary) {
        this.updateForm(promoterItinerary);
        this.loadStopPoints();
      }
    });

    this.loadRelationshipsOptions();
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;

    if(this.promoterItinerary){
      this.editForm.controls.promoterItineraryStopPoints.patchValue(this.promoterItinerary.promoterItineraryStopPoints);

      this.promoterItineraryStopPointsToDelete.forEach(id => {
        this.promoterStopPointService.delete(id).subscribe();
      })
    }

    const promoterItinerary = this.promoterItineraryFormService.getPromoterItinerary(this.editForm);

    if (promoterItinerary.id !== null) {
      this.subscribeToSaveResponse(this.promoterItineraryService.update(promoterItinerary));
    } else {
      this.subscribeToSaveResponse(this.promoterItineraryService.create(promoterItinerary));
    }
  }

  deleteItinerary(){
    this.promoterItinerary.promoterItineraryStopPoints.forEach(stopPoint => {
      this.promoterStopPointService.delete(stopPoint.id).subscribe();
    })

    this.childItinerarySubscriptionService.delete(this.promoterItinerary.id).subscribe({
      next: () => {
        this.promoterItineraryService.delete(this.promoterItinerary.id).subscribe({
          next: () => this.router.navigate(['operator/promoter-itineraries']),
          error: () => this.onSaveError(),
        });
      }
    })
  }

  addItineraryStopPoint(){
    const modal = this.modalService.open(PromoterStopPointSelectDialogComponent, { size: 'lg' });

    modal.componentInstance.promoterItinerary = this.promoterItinerary;

    modal.result.then(result => {
      this.promoterItinerary.promoterItineraryStopPoints.push(result);
    }).catch(() => {});
  }

  changePositionStopPoint(event: CdkDragDrop<string[]>){
    const tempStopPoint = this.promoterItinerary.promoterItineraryStopPoints[event.previousIndex];

    this.promoterItinerary.promoterItineraryStopPoints.splice(event.previousIndex, 1);

    this.promoterItinerary.promoterItineraryStopPoints.splice(event.currentIndex, 0, tempStopPoint);

    //fazer order por hora aqui?
  }

  buildAddress(location: ILocation) {
    return BuildAddress.buildAddressFromLocationObject(location);
  }

  deleteWrapper(id: number, index: number){
    this.promoterItineraryStopPointsToDelete.push(id);

    this.promoterItinerary.promoterItineraryStopPoints.splice(index, 1);
  }

  loadStopPoints() {
    this.promoterStopPointService
      .getPromoterStopPoints(this.promoterItinerary.id)
      .subscribe(promoterStopPoints => {
        this.promoterItinerary.promoterItineraryStopPoints = promoterStopPoints.body;

        this.promoterItinerary.promoterItineraryStopPoints.forEach(stopPoint => {
          this.locationService.find(stopPoint.location.id).subscribe(res => {
            stopPoint.location = res.body;
          })
        })
      });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPromoterItinerary>>): void {
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

  protected updateForm(promoterItinerary: IPromoterItinerary): void {
    this.promoterItinerary = promoterItinerary;
    this.promoterItineraryFormService.resetForm(this.editForm, promoterItinerary);

    this.promoterServicesSharedCollection = this.promoterServiceService.addPromoterServiceToCollectionIfMissing<IPromoterService>(
      this.promoterServicesSharedCollection,
      promoterItinerary.service
    );
  }

  protected loadRelationshipsOptions(): void {
    this.promoterServiceService
      .query()
      .pipe(map((res: HttpResponse<IPromoterService[]>) => res.body ?? []))
      .pipe(
        map((promoterServices: IPromoterService[]) =>
          this.promoterServiceService.addPromoterServiceToCollectionIfMissing<IPromoterService>(
            promoterServices,
            this.promoterItinerary?.service
          )
        )
      )
      .subscribe((promoterServices: IPromoterService[]) => {
        this.promoterServicesSharedCollection = promoterServices;
      });
  }
}
