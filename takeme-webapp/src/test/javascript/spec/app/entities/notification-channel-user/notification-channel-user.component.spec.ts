/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { NotificationChannelUserComponent } from 'app/entities/notification-channel-user/notification-channel-user.component';
import { NotificationChannelUserService } from 'app/entities/notification-channel-user/notification-channel-user.service';
import { NotificationChannelUser } from 'app/shared/model/notification-channel-user.model';

describe('Component Tests', () => {
    describe('NotificationChannelUser Management Component', () => {
        let comp: NotificationChannelUserComponent;
        let fixture: ComponentFixture<NotificationChannelUserComponent>;
        let service: NotificationChannelUserService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [NotificationChannelUserComponent],
                providers: []
            })
                .overrideTemplate(NotificationChannelUserComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(NotificationChannelUserComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotificationChannelUserService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new NotificationChannelUser(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.notificationChannelUsers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
