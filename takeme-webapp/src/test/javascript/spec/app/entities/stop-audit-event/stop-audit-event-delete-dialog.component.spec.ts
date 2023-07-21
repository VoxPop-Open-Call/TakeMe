/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { StopAuditEventDeleteDialogComponent } from 'app/entities/stop-audit-event/stop-audit-event-delete-dialog.component';
import { StopAuditEventService } from 'app/entities/stop-audit-event/stop-audit-event.service';

describe('Component Tests', () => {
    describe('StopAuditEvent Management Delete Component', () => {
        let comp: StopAuditEventDeleteDialogComponent;
        let fixture: ComponentFixture<StopAuditEventDeleteDialogComponent>;
        let service: StopAuditEventService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [StopAuditEventDeleteDialogComponent]
            })
                .overrideTemplate(StopAuditEventDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(StopAuditEventDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StopAuditEventService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});