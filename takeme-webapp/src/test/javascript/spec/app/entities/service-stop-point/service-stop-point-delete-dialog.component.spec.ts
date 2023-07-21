/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ServiceStopPointDeleteDialogComponent } from 'app/entities/service-stop-point/service-stop-point-delete-dialog.component';
import { ServiceStopPointService } from 'app/entities/service-stop-point/service-stop-point.service';

describe('Component Tests', () => {
    describe('ServiceStopPoint Management Delete Component', () => {
        let comp: ServiceStopPointDeleteDialogComponent;
        let fixture: ComponentFixture<ServiceStopPointDeleteDialogComponent>;
        let service: ServiceStopPointService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ServiceStopPointDeleteDialogComponent]
            })
                .overrideTemplate(ServiceStopPointDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ServiceStopPointDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ServiceStopPointService);
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
