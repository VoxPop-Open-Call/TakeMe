/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ServiceStopPointDaysOfWeekDeleteDialogComponent } from 'app/entities/service-stop-point-days-of-week/service-stop-point-days-of-week-delete-dialog.component';
import { ServiceStopPointDaysOfWeekService } from 'app/entities/service-stop-point-days-of-week/service-stop-point-days-of-week.service';

describe('Component Tests', () => {
    describe('ServiceStopPointDaysOfWeek Management Delete Component', () => {
        let comp: ServiceStopPointDaysOfWeekDeleteDialogComponent;
        let fixture: ComponentFixture<ServiceStopPointDaysOfWeekDeleteDialogComponent>;
        let service: ServiceStopPointDaysOfWeekService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ServiceStopPointDaysOfWeekDeleteDialogComponent]
            })
                .overrideTemplate(ServiceStopPointDaysOfWeekDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ServiceStopPointDaysOfWeekDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ServiceStopPointDaysOfWeekService);
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
