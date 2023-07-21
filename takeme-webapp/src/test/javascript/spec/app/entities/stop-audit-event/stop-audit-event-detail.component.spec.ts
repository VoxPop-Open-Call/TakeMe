/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { StopAuditEventDetailComponent } from 'app/entities/stop-audit-event/stop-audit-event-detail.component';
import { StopAuditEvent } from 'app/shared/model/stop-audit-event.model';

describe('Component Tests', () => {
    describe('StopAuditEvent Management Detail Component', () => {
        let comp: StopAuditEventDetailComponent;
        let fixture: ComponentFixture<StopAuditEventDetailComponent>;
        const route = ({ data: of({ stopAuditEvent: new StopAuditEvent(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [StopAuditEventDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(StopAuditEventDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(StopAuditEventDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.stopAuditEvent).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
