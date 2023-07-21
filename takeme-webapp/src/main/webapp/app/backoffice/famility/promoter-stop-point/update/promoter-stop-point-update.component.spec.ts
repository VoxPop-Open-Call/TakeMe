import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PromoterStopPointFormService } from './promoter-stop-point-form.service';
import { PromoterStopPointService } from '../service/promoter-stop-point.service';
import { IPromoterStopPoint } from '../promoter-stop-point.model';
import { IPromoterItinerary } from 'app/backoffice/famility/promoter-itinerary/promoter-itinerary.model';
import { PromoterItineraryService } from 'app/backoffice/famility/promoter-itinerary/service/promoter-itinerary.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';

import { PromoterStopPointUpdateComponent } from './promoter-stop-point-update.component';

describe('PromoterStopPoint Management Update Component', () => {
  let comp: PromoterStopPointUpdateComponent;
  let fixture: ComponentFixture<PromoterStopPointUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let promoterStopPointFormService: PromoterStopPointFormService;
  let promoterStopPointService: PromoterStopPointService;
  let promoterItineraryService: PromoterItineraryService;
  let locationService: LocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PromoterStopPointUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PromoterStopPointUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PromoterStopPointUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    promoterStopPointFormService = TestBed.inject(PromoterStopPointFormService);
    promoterStopPointService = TestBed.inject(PromoterStopPointService);
    promoterItineraryService = TestBed.inject(PromoterItineraryService);
    locationService = TestBed.inject(LocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PromoterItinerary query and add missing value', () => {
      const promoterStopPoint: IPromoterStopPoint = { id: 456 };
      const promoterItinerary: IPromoterItinerary = { id: 87735 };
      promoterStopPoint.promoterItinerary = promoterItinerary;

      const promoterItineraryCollection: IPromoterItinerary[] = [{ id: 78760 }];
      jest.spyOn(promoterItineraryService, 'query').mockReturnValue(of(new HttpResponse({ body: promoterItineraryCollection })));
      const additionalPromoterItineraries = [promoterItinerary];
      const expectedCollection: IPromoterItinerary[] = [...additionalPromoterItineraries, ...promoterItineraryCollection];
      jest.spyOn(promoterItineraryService, 'addPromoterItineraryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ promoterStopPoint });
      comp.ngOnInit();

      expect(promoterItineraryService.query).toHaveBeenCalled();
      expect(promoterItineraryService.addPromoterItineraryToCollectionIfMissing).toHaveBeenCalledWith(
        promoterItineraryCollection,
        ...additionalPromoterItineraries.map(expect.objectContaining)
      );
      expect(comp.promoterItinerariesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Location query and add missing value', () => {
      const promoterStopPoint: IPromoterStopPoint = { id: 456 };
      const location: ILocation = { id: 80681 };
      promoterStopPoint.location = location;

      const locationCollection: ILocation[] = [{ id: 44475 }];
      jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
      const additionalLocations = [location];
      const expectedCollection: ILocation[] = [...additionalLocations, ...locationCollection];
      jest.spyOn(locationService, 'addLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ promoterStopPoint });
      comp.ngOnInit();

      expect(locationService.query).toHaveBeenCalled();
      expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(
        locationCollection,
        ...additionalLocations.map(expect.objectContaining)
      );
      expect(comp.locationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const promoterStopPoint: IPromoterStopPoint = { id: 456 };
      const promoterItinerary: IPromoterItinerary = { id: 18581 };
      promoterStopPoint.promoterItinerary = promoterItinerary;
      const location: ILocation = { id: 70542 };
      promoterStopPoint.location = location;

      activatedRoute.data = of({ promoterStopPoint });
      comp.ngOnInit();

      expect(comp.promoterItinerariesSharedCollection).toContain(promoterItinerary);
      expect(comp.locationsSharedCollection).toContain(location);
      expect(comp.promoterStopPoint).toEqual(promoterStopPoint);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPromoterStopPoint>>();
      const promoterStopPoint = { id: 123 };
      jest.spyOn(promoterStopPointFormService, 'getPromoterStopPoint').mockReturnValue(promoterStopPoint);
      jest.spyOn(promoterStopPointService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promoterStopPoint });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: promoterStopPoint }));
      saveSubject.complete();

      // THEN
      expect(promoterStopPointFormService.getPromoterStopPoint).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(promoterStopPointService.update).toHaveBeenCalledWith(expect.objectContaining(promoterStopPoint));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPromoterStopPoint>>();
      const promoterStopPoint = { id: 123 };
      jest.spyOn(promoterStopPointFormService, 'getPromoterStopPoint').mockReturnValue({ id: null });
      jest.spyOn(promoterStopPointService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promoterStopPoint: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: promoterStopPoint }));
      saveSubject.complete();

      // THEN
      expect(promoterStopPointFormService.getPromoterStopPoint).toHaveBeenCalled();
      expect(promoterStopPointService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPromoterStopPoint>>();
      const promoterStopPoint = { id: 123 };
      jest.spyOn(promoterStopPointService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promoterStopPoint });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(promoterStopPointService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePromoterItinerary', () => {
      it('Should forward to promoterItineraryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(promoterItineraryService, 'comparePromoterItinerary');
        comp.comparePromoterItinerary(entity, entity2);
        expect(promoterItineraryService.comparePromoterItinerary).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareLocation', () => {
      it('Should forward to locationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(locationService, 'compareLocation');
        comp.compareLocation(entity, entity2);
        expect(locationService.compareLocation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
