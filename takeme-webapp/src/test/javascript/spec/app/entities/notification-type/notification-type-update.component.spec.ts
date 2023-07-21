/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { NotificationTypeUpdateComponent } from 'app/entities/notification-type/notification-type-update.component';
import { NotificationTypeService } from 'app/entities/notification-type/notification-type.service';
import { NotificationType } from 'app/shared/model/notification-type.model';

describe('Component Tests', () => {
    describe('NotificationType Management Update Component', () => {
        let comp: NotificationTypeUpdateComponent;
        let fixture: ComponentFixture<NotificationTypeUpdateComponent>;
        let service: NotificationTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [NotificationTypeUpdateComponent]
            })
                .overrideTemplate(NotificationTypeUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(NotificationTypeUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotificationTypeService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new NotificationType(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.notificationType = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new NotificationType();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.notificationType = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
