import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChildItinerarySubscriptionDetailComponent } from './child-itinerary-subscription-detail.component';

describe('ChildItinerarySubscription Management Detail Component', () => {
  let comp: ChildItinerarySubscriptionDetailComponent;
  let fixture: ComponentFixture<ChildItinerarySubscriptionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChildItinerarySubscriptionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ childItinerarySubscription: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ChildItinerarySubscriptionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ChildItinerarySubscriptionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load childItinerarySubscription on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.childItinerarySubscription).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
