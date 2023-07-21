/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { StopAuditEventComponent } from 'app/entities/stop-audit-event/stop-audit-event.component';
import { StopAuditEventService } from 'app/entities/stop-audit-event/stop-audit-event.service';
import { StopAuditEvent } from 'app/shared/model/stop-audit-event.model';

describe('Component Tests', () => {
    describe('StopAuditEvent Management Component', () => {
        let comp: StopAuditEventComponent;
        let fixture: ComponentFixture<StopAuditEventComponent>;
        let service: StopAuditEventService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [StopAuditEventComponent],
                providers: []
            })
                .overrideTemplate(StopAuditEventComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(StopAuditEventComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StopAuditEventService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new StopAuditEvent(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.stopAuditEvents[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
