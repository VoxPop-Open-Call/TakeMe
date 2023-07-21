/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ServiceStopPointDaysOfWeekUpdateComponent } from 'app/entities/service-stop-point-days-of-week/service-stop-point-days-of-week-update.component';
import { ServiceStopPointDaysOfWeekService } from 'app/entities/service-stop-point-days-of-week/service-stop-point-days-of-week.service';
import { ServiceStopPointDaysOfWeek } from 'app/shared/model/service-stop-point-days-of-week.model';

describe('Component Tests', () => {
    describe('ServiceStopPointDaysOfWeek Management Update Component', () => {
        let comp: ServiceStopPointDaysOfWeekUpdateComponent;
        let fixture: ComponentFixture<ServiceStopPointDaysOfWeekUpdateComponent>;
        let service: ServiceStopPointDaysOfWeekService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ServiceStopPointDaysOfWeekUpdateComponent]
            })
                .overrideTemplate(ServiceStopPointDaysOfWeekUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ServiceStopPointDaysOfWeekUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ServiceStopPointDaysOfWeekService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new ServiceStopPointDaysOfWeek(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.serviceStopPointDaysOfWeek = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new ServiceStopPointDaysOfWeek();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.serviceStopPointDaysOfWeek = entity;
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
