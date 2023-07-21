/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ServiceStopPointFrequencyUpdateComponent } from 'app/entities/service-stop-point-frequency/service-stop-point-frequency-update.component';
import { ServiceStopPointFrequencyService } from 'app/entities/service-stop-point-frequency/service-stop-point-frequency.service';
import { ServiceStopPointFrequency } from 'app/shared/model/service-stop-point-frequency.model';

describe('Component Tests', () => {
    describe('ServiceStopPointFrequency Management Update Component', () => {
        let comp: ServiceStopPointFrequencyUpdateComponent;
        let fixture: ComponentFixture<ServiceStopPointFrequencyUpdateComponent>;
        let service: ServiceStopPointFrequencyService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ServiceStopPointFrequencyUpdateComponent]
            })
                .overrideTemplate(ServiceStopPointFrequencyUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ServiceStopPointFrequencyUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ServiceStopPointFrequencyService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new ServiceStopPointFrequency(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.serviceStopPointFrequency = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new ServiceStopPointFrequency();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.serviceStopPointFrequency = entity;
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
