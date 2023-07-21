/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { NotificationChannelUserUpdateComponent } from 'app/entities/notification-channel-user/notification-channel-user-update.component';
import { NotificationChannelUserService } from 'app/entities/notification-channel-user/notification-channel-user.service';
import { NotificationChannelUser } from 'app/shared/model/notification-channel-user.model';

describe('Component Tests', () => {
    describe('NotificationChannelUser Management Update Component', () => {
        let comp: NotificationChannelUserUpdateComponent;
        let fixture: ComponentFixture<NotificationChannelUserUpdateComponent>;
        let service: NotificationChannelUserService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [NotificationChannelUserUpdateComponent]
            })
                .overrideTemplate(NotificationChannelUserUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(NotificationChannelUserUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotificationChannelUserService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new NotificationChannelUser(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.notificationChannelUser = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new NotificationChannelUser();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.notificationChannelUser = entity;
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
