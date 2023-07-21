import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ChildItinerarySubscriptionFormService } from './child-itinerary-subscription-form.service';
import { ChildItinerarySubscriptionService } from '../service/child-itinerary-subscription.service';
import { IChildItinerarySubscription } from '../child-itinerary-subscription.model';
import { IChild } from 'app/entities/child/child.model';
import { ChildService } from 'app/entities/child/service/child.service';
import { IPromoterItinerary } from 'app/entities/promoter-itinerary/promoter-itinerary.model';
import { PromoterItineraryService } from 'app/entities/promoter-itinerary/service/promoter-itinerary.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { ChildItinerarySubscriptionUpdateComponent } from './child-itinerary-subscription-update.component';

describe('ChildItinerarySubscription Management Update Component', () => {
  let comp: ChildItinerarySubscriptionUpdateComponent;
  let fixture: ComponentFixture<ChildItinerarySubscriptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let childItinerarySubscriptionFormService: ChildItinerarySubscriptionFormService;
  let childItinerarySubscriptionService: ChildItinerarySubscriptionService;
  let childService: ChildService;
  let promoterItineraryService: PromoterItineraryService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ChildItinerarySubscriptionUpdateComponent],
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
      .overrideTemplate(ChildItinerarySubscriptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChildItinerarySubscriptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    childItinerarySubscriptionFormService = TestBed.inject(ChildItinerarySubscriptionFormService);
    childItinerarySubscriptionService = TestBed.inject(ChildItinerarySubscriptionService);
    childService = TestBed.inject(ChildService);
    promoterItineraryService = TestBed.inject(PromoterItineraryService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Child query and add missing value', () => {
      const childItinerarySubscription: IChildItinerarySubscription = { id: 456 };
      const child: IChild = { id: 35998 };
      childItinerarySubscription.child = child;

      const childCollection: IChild[] = [{ id: 63197 }];
      jest.spyOn(childService, 'query').mockReturnValue(of(new HttpResponse({ body: childCollection })));
      const additionalChildren = [child];
      const expectedCollection: IChild[] = [...additionalChildren, ...childCollection];
      jest.spyOn(childService, 'addChildToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ childItinerarySubscription });
      comp.ngOnInit();

      expect(childService.query).toHaveBeenCalled();
      expect(childService.addChildToCollectionIfMissing).toHaveBeenCalledWith(
        childCollection,
        ...additionalChildren.map(expect.objectContaining)
      );
      expect(comp.childrenSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PromoterItinerary query and add missing value', () => {
      const childItinerarySubscription: IChildItinerarySubscription = { id: 456 };
      const promoterItinerary: IPromoterItinerary = { id: 12674 };
      childItinerarySubscription.promoterItinerary = promoterItinerary;

      const promoterItineraryCollection: IPromoterItinerary[] = [{ id: 98214 }];
      jest.spyOn(promoterItineraryService, 'query').mockReturnValue(of(new HttpResponse({ body: promoterItineraryCollection })));
      const additionalPromoterItineraries = [promoterItinerary];
      const expectedCollection: IPromoterItinerary[] = [...additionalPromoterItineraries, ...promoterItineraryCollection];
      jest.spyOn(promoterItineraryService, 'addPromoterItineraryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ childItinerarySubscription });
      comp.ngOnInit();

      expect(promoterItineraryService.query).toHaveBeenCalled();
      expect(promoterItineraryService.addPromoterItineraryToCollectionIfMissing).toHaveBeenCalledWith(
        promoterItineraryCollection,
        ...additionalPromoterItineraries.map(expect.objectContaining)
      );
      expect(comp.promoterItinerariesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const childItinerarySubscription: IChildItinerarySubscription = { id: 456 };
      const user: IUser = { id: 27819 };
      childItinerarySubscription.user = user;

      const userCollection: IUser[] = [{ id: 1119 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ childItinerarySubscription });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const childItinerarySubscription: IChildItinerarySubscription = { id: 456 };
      const child: IChild = { id: 58904 };
      childItinerarySubscription.child = child;
      const promoterItinerary: IPromoterItinerary = { id: 54240 };
      childItinerarySubscription.promoterItinerary = promoterItinerary;
      const user: IUser = { id: 86913 };
      childItinerarySubscription.user = user;

      activatedRoute.data = of({ childItinerarySubscription });
      comp.ngOnInit();

      expect(comp.childrenSharedCollection).toContain(child);
      expect(comp.promoterItinerariesSharedCollection).toContain(promoterItinerary);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.childItinerarySubscription).toEqual(childItinerarySubscription);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChildItinerarySubscription>>();
      const childItinerarySubscription = { id: 123 };
      jest.spyOn(childItinerarySubscriptionFormService, 'getChildItinerarySubscription').mockReturnValue(childItinerarySubscription);
      jest.spyOn(childItinerarySubscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ childItinerarySubscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: childItinerarySubscription }));
      saveSubject.complete();

      // THEN
      expect(childItinerarySubscriptionFormService.getChildItinerarySubscription).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(childItinerarySubscriptionService.update).toHaveBeenCalledWith(expect.objectContaining(childItinerarySubscription));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChildItinerarySubscription>>();
      const childItinerarySubscription = { id: 123 };
      jest.spyOn(childItinerarySubscriptionFormService, 'getChildItinerarySubscription').mockReturnValue({ id: null });
      jest.spyOn(childItinerarySubscriptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ childItinerarySubscription: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: childItinerarySubscription }));
      saveSubject.complete();

      // THEN
      expect(childItinerarySubscriptionFormService.getChildItinerarySubscription).toHaveBeenCalled();
      expect(childItinerarySubscriptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChildItinerarySubscription>>();
      const childItinerarySubscription = { id: 123 };
      jest.spyOn(childItinerarySubscriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ childItinerarySubscription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(childItinerarySubscriptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareChild', () => {
      it('Should forward to childService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(childService, 'compareChild');
        comp.compareChild(entity, entity2);
        expect(childService.compareChild).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePromoterItinerary', () => {
      it('Should forward to promoterItineraryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(promoterItineraryService, 'comparePromoterItinerary');
        comp.comparePromoterItinerary(entity, entity2);
        expect(promoterItineraryService.comparePromoterItinerary).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
