import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PromoterItineraryFormService } from './promoter-itinerary-form.service';
import { OperatorPromoterItineraryService } from '../service/promoter-itinerary.service';
import { IPromoterItinerary } from '../promoter-itinerary.model';
import { IPromoterService } from '../../../famility/promoter-service/promoter-service.model';
import { PromoterServiceService } from '../../../famility/promoter-service/service/promoter-service.service';

import { PromoterItineraryUpdateComponent } from './promoter-itinerary-update.component';

describe('PromoterItinerary Management Update Component', () => {
  let comp: PromoterItineraryUpdateComponent;
  let fixture: ComponentFixture<PromoterItineraryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let promoterItineraryFormService: PromoterItineraryFormService;
  let promoterItineraryService: OperatorPromoterItineraryService;
  let promoterServiceService: PromoterServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PromoterItineraryUpdateComponent],
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
      .overrideTemplate(PromoterItineraryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PromoterItineraryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    promoterItineraryFormService = TestBed.inject(PromoterItineraryFormService);
    promoterItineraryService = TestBed.inject(OperatorPromoterItineraryService);
    promoterServiceService = TestBed.inject(PromoterServiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PromoterService query and add missing value', () => {
      const promoterItinerary: IPromoterItinerary = { id: 456 };
      const service: IPromoterService = { id: 67258 };
      promoterItinerary.service = service;

      const promoterServiceCollection: IPromoterService[] = [{ id: 13260 }];
      jest.spyOn(promoterServiceService, 'query').mockReturnValue(of(new HttpResponse({ body: promoterServiceCollection })));
      const additionalPromoterServices = [service];
      const expectedCollection: IPromoterService[] = [...additionalPromoterServices, ...promoterServiceCollection];
      jest.spyOn(promoterServiceService, 'addPromoterServiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ promoterItinerary });
      comp.ngOnInit();

      expect(promoterServiceService.query).toHaveBeenCalled();
      expect(promoterServiceService.addPromoterServiceToCollectionIfMissing).toHaveBeenCalledWith(
        promoterServiceCollection,
        ...additionalPromoterServices.map(expect.objectContaining)
      );
      expect(comp.promoterServicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const promoterItinerary: IPromoterItinerary = { id: 456 };
      const service: IPromoterService = { id: 79266 };
      promoterItinerary.service = service;

      activatedRoute.data = of({ promoterItinerary });
      comp.ngOnInit();

      expect(comp.promoterServicesSharedCollection).toContain(service);
      expect(comp.promoterItinerary).toEqual(promoterItinerary);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPromoterItinerary>>();
      const promoterItinerary = { id: 123 };
      jest.spyOn(promoterItineraryFormService, 'getPromoterItinerary').mockReturnValue(promoterItinerary);
      jest.spyOn(promoterItineraryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promoterItinerary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: promoterItinerary }));
      saveSubject.complete();

      // THEN
      expect(promoterItineraryFormService.getPromoterItinerary).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(promoterItineraryService.update).toHaveBeenCalledWith(expect.objectContaining(promoterItinerary));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPromoterItinerary>>();
      const promoterItinerary = { id: 123 };
      jest.spyOn(promoterItineraryFormService, 'getPromoterItinerary').mockReturnValue({ id: null });
      jest.spyOn(promoterItineraryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promoterItinerary: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: promoterItinerary }));
      saveSubject.complete();

      // THEN
      expect(promoterItineraryFormService.getPromoterItinerary).toHaveBeenCalled();
      expect(promoterItineraryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPromoterItinerary>>();
      const promoterItinerary = { id: 123 };
      jest.spyOn(promoterItineraryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ promoterItinerary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(promoterItineraryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePromoterService', () => {
      it('Should forward to promoterServiceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(promoterServiceService, 'comparePromoterService');
        comp.comparePromoterService(entity, entity2);
        expect(promoterServiceService.comparePromoterService).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
