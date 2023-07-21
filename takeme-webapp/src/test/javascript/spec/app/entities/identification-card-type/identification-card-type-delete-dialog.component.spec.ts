/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { IdentificationCardTypeDeleteDialogComponent } from 'app/entities/identification-card-type/identification-card-type-delete-dialog.component';
import { IdentificationCardTypeService } from 'app/entities/identification-card-type/identification-card-type.service';

describe('Component Tests', () => {
    describe('IdentificationCardType Management Delete Component', () => {
        let comp: IdentificationCardTypeDeleteDialogComponent;
        let fixture: ComponentFixture<IdentificationCardTypeDeleteDialogComponent>;
        let service: IdentificationCardTypeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [IdentificationCardTypeDeleteDialogComponent]
            })
                .overrideTemplate(IdentificationCardTypeDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(IdentificationCardTypeDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IdentificationCardTypeService);
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
