/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { StopPointUpdateComponent } from 'app/entities/stop-point/stop-point-update.component';
import { StopPointService } from 'app/entities/stop-point/stop-point.service';
import { StopPoint } from 'app/shared/model/stop-point.model';

describe('Component Tests', () => {
    describe('StopPoint Management Update Component', () => {
        let comp: StopPointUpdateComponent;
        let fixture: ComponentFixture<StopPointUpdateComponent>;
        let service: StopPointService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [StopPointUpdateComponent]
            })
                .overrideTemplate(StopPointUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(StopPointUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StopPointService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new StopPoint(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.stopPoint = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new StopPoint();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.stopPoint = entity;
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
