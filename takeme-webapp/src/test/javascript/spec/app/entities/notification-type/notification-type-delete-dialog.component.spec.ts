/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { NotificationTypeDeleteDialogComponent } from 'app/entities/notification-type/notification-type-delete-dialog.component';
import { NotificationTypeService } from 'app/entities/notification-type/notification-type.service';

describe('Component Tests', () => {
    describe('NotificationType Management Delete Component', () => {
        let comp: NotificationTypeDeleteDialogComponent;
        let fixture: ComponentFixture<NotificationTypeDeleteDialogComponent>;
        let service: NotificationTypeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [NotificationTypeDeleteDialogComponent]
            })
                .overrideTemplate(NotificationTypeDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(NotificationTypeDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotificationTypeService);
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
