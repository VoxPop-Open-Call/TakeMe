import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PromoterStopPointDetailComponent } from './promoter-stop-point-detail.component';

describe('PromoterStopPoint Management Detail Component', () => {
  let comp: PromoterStopPointDetailComponent;
  let fixture: ComponentFixture<PromoterStopPointDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PromoterStopPointDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ promoterStopPoint: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PromoterStopPointDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PromoterStopPointDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load promoterStopPoint on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.promoterStopPoint).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
