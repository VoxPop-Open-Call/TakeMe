/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ServiceStopPointFrequencyDeleteDialogComponent } from 'app/entities/service-stop-point-frequency/service-stop-point-frequency-delete-dialog.component';
import { ServiceStopPointFrequencyService } from 'app/entities/service-stop-point-frequency/service-stop-point-frequency.service';

describe('Component Tests', () => {
    describe('ServiceStopPointFrequency Management Delete Component', () => {
        let comp: ServiceStopPointFrequencyDeleteDialogComponent;
        let fixture: ComponentFixture<ServiceStopPointFrequencyDeleteDialogComponent>;
        let service: ServiceStopPointFrequencyService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ServiceStopPointFrequencyDeleteDialogComponent]
            })
                .overrideTemplate(ServiceStopPointFrequencyDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ServiceStopPointFrequencyDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ServiceStopPointFrequencyService);
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
