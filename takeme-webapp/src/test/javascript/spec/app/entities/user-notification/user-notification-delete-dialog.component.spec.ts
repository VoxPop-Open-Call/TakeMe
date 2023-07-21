/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { UserNotificationDeleteDialogComponent } from 'app/entities/user-notification/user-notification-delete-dialog.component';
import { UserNotificationService } from 'app/entities/user-notification/user-notification.service';

describe('Component Tests', () => {
    describe('UserNotification Management Delete Component', () => {
        let comp: UserNotificationDeleteDialogComponent;
        let fixture: ComponentFixture<UserNotificationDeleteDialogComponent>;
        let service: UserNotificationService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [UserNotificationDeleteDialogComponent]
            })
                .overrideTemplate(UserNotificationDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserNotificationDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserNotificationService);
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
