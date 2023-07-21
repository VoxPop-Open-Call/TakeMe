/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ItineraryDetailComponent } from 'app/entities/itinerary/itinerary-detail.component';
import { Itinerary } from 'app/shared/model/itinerary.model';

describe('Component Tests', () => {
    describe('Itinerary Management Detail Component', () => {
        let comp: ItineraryDetailComponent;
        let fixture: ComponentFixture<ItineraryDetailComponent>;
        const route = ({ data: of({ itinerary: new Itinerary(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ItineraryDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ItineraryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ItineraryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.itinerary).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
