/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ServiceStopPointUpdateComponent } from 'app/entities/service-stop-point/service-stop-point-update.component';
import { ServiceStopPointService } from 'app/entities/service-stop-point/service-stop-point.service';
import { ServiceStopPoint } from 'app/shared/model/service-stop-point.model';

describe('Component Tests', () => {
    describe('ServiceStopPoint Management Update Component', () => {
        let comp: ServiceStopPointUpdateComponent;
        let fixture: ComponentFixture<ServiceStopPointUpdateComponent>;
        let service: ServiceStopPointService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ServiceStopPointUpdateComponent]
            })
                .overrideTemplate(ServiceStopPointUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ServiceStopPointUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ServiceStopPointService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new ServiceStopPoint(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.serviceStopPoint = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new ServiceStopPoint();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.serviceStopPoint = entity;
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
