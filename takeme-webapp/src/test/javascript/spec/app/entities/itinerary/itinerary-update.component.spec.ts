/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ItineraryUpdateComponent } from 'app/entities/itinerary/itinerary-update.component';
import { ItineraryService } from 'app/entities/itinerary/itinerary.service';
import { Itinerary } from 'app/shared/model/itinerary.model';

describe('Component Tests', () => {
    describe('Itinerary Management Update Component', () => {
        let comp: ItineraryUpdateComponent;
        let fixture: ComponentFixture<ItineraryUpdateComponent>;
        let service: ItineraryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ItineraryUpdateComponent]
            })
                .overrideTemplate(ItineraryUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ItineraryUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ItineraryService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Itinerary(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.itinerary = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Itinerary();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.itinerary = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
