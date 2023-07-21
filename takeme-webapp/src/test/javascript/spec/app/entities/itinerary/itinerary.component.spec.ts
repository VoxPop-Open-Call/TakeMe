/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ItineraryComponent } from 'app/entities/itinerary/itinerary.component';
import { ItineraryService } from 'app/entities/itinerary/itinerary.service';
import { Itinerary } from 'app/shared/model/itinerary.model';

describe('Component Tests', () => {
    describe('Itinerary Management Component', () => {
        let comp: ItineraryComponent;
        let fixture: ComponentFixture<ItineraryComponent>;
        let service: ItineraryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ItineraryComponent],
                providers: []
            })
                .overrideTemplate(ItineraryComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ItineraryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ItineraryService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Itinerary(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.itineraries[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
