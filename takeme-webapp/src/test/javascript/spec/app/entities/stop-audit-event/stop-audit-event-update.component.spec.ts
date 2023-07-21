/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { StopAuditEventUpdateComponent } from 'app/entities/stop-audit-event/stop-audit-event-update.component';
import { StopAuditEventService } from 'app/entities/stop-audit-event/stop-audit-event.service';
import { StopAuditEvent } from 'app/shared/model/stop-audit-event.model';

describe('Component Tests', () => {
    describe('StopAuditEvent Management Update Component', () => {
        let comp: StopAuditEventUpdateComponent;
        let fixture: ComponentFixture<StopAuditEventUpdateComponent>;
        let service: StopAuditEventService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [StopAuditEventUpdateComponent]
            })
                .overrideTemplate(StopAuditEventUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(StopAuditEventUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StopAuditEventService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new StopAuditEvent(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.stopAuditEvent = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new StopAuditEvent();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.stopAuditEvent = entity;
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
