import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PromoterItineraryDetailComponent } from './promoter-itinerary-detail.component';

describe('PromoterItinerary Management Detail Component', () => {
  let comp: PromoterItineraryDetailComponent;
  let fixture: ComponentFixture<PromoterItineraryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PromoterItineraryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ promoterItinerary: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PromoterItineraryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PromoterItineraryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load promoterItinerary on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.promoterItinerary).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
