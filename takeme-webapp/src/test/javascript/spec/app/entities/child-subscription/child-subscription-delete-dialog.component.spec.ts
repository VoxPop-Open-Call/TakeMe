/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ChildSubscriptionDeleteDialogComponent } from 'app/entities/child-subscription/child-subscription-delete-dialog.component';
import { ChildSubscriptionService } from 'app/entities/child-subscription/child-subscription.service';

describe('Component Tests', () => {
    describe('ChildSubscription Management Delete Component', () => {
        let comp: ChildSubscriptionDeleteDialogComponent;
        let fixture: ComponentFixture<ChildSubscriptionDeleteDialogComponent>;
        let service: ChildSubscriptionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ChildSubscriptionDeleteDialogComponent]
            })
                .overrideTemplate(ChildSubscriptionDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ChildSubscriptionDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ChildSubscriptionService);
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
