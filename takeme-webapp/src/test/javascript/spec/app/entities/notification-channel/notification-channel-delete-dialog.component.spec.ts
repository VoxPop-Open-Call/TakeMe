/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { NotificationChannelDeleteDialogComponent } from 'app/entities/notification-channel/notification-channel-delete-dialog.component';
import { NotificationChannelService } from 'app/entities/notification-channel/notification-channel.service';

describe('Component Tests', () => {
    describe('NotificationChannel Management Delete Component', () => {
        let comp: NotificationChannelDeleteDialogComponent;
        let fixture: ComponentFixture<NotificationChannelDeleteDialogComponent>;
        let service: NotificationChannelService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [NotificationChannelDeleteDialogComponent]
            })
                .overrideTemplate(NotificationChannelDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(NotificationChannelDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotificationChannelService);
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
