/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { UserMessagingTokenDeleteDialogComponent } from 'app/entities/user-messaging-token/user-messaging-token-delete-dialog.component';
import { UserMessagingTokenService } from 'app/entities/user-messaging-token/user-messaging-token.service';

describe('Component Tests', () => {
    describe('UserMessagingToken Management Delete Component', () => {
        let comp: UserMessagingTokenDeleteDialogComponent;
        let fixture: ComponentFixture<UserMessagingTokenDeleteDialogComponent>;
        let service: UserMessagingTokenService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [UserMessagingTokenDeleteDialogComponent]
            })
                .overrideTemplate(UserMessagingTokenDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserMessagingTokenDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserMessagingTokenService);
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
