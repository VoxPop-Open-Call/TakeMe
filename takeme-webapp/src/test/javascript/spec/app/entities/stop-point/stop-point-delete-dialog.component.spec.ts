/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { StopPointDeleteDialogComponent } from 'app/entities/stop-point/stop-point-delete-dialog.component';
import { StopPointService } from 'app/entities/stop-point/stop-point.service';

describe('Component Tests', () => {
    describe('StopPoint Management Delete Component', () => {
        let comp: StopPointDeleteDialogComponent;
        let fixture: ComponentFixture<StopPointDeleteDialogComponent>;
        let service: StopPointService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [StopPointDeleteDialogComponent]
            })
                .overrideTemplate(StopPointDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(StopPointDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StopPointService);
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
