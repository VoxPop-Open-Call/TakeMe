/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { UserNotificationUpdateComponent } from 'app/entities/user-notification/user-notification-update.component';
import { UserNotificationService } from 'app/entities/user-notification/user-notification.service';
import { UserNotification } from 'app/shared/model/user-notification.model';

describe('Component Tests', () => {
    describe('UserNotification Management Update Component', () => {
        let comp: UserNotificationUpdateComponent;
        let fixture: ComponentFixture<UserNotificationUpdateComponent>;
        let service: UserNotificationService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [UserNotificationUpdateComponent]
            })
                .overrideTemplate(UserNotificationUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UserNotificationUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserNotificationService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new UserNotification(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.userNotification = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new UserNotification();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.userNotification = entity;
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
