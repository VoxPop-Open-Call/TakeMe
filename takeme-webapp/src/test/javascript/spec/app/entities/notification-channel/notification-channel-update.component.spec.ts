/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { NotificationChannelUpdateComponent } from 'app/entities/notification-channel/notification-channel-update.component';
import { NotificationChannelService } from 'app/entities/notification-channel/notification-channel.service';
import { NotificationChannel } from 'app/shared/model/notification-channel.model';

describe('Component Tests', () => {
    describe('NotificationChannel Management Update Component', () => {
        let comp: NotificationChannelUpdateComponent;
        let fixture: ComponentFixture<NotificationChannelUpdateComponent>;
        let service: NotificationChannelService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [NotificationChannelUpdateComponent]
            })
                .overrideTemplate(NotificationChannelUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(NotificationChannelUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotificationChannelService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new NotificationChannel(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.notificationChannel = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new NotificationChannel();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.notificationChannel = entity;
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
