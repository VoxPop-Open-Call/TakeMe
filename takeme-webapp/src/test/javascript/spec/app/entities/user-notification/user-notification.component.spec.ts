/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { UserNotificationComponent } from 'app/entities/user-notification/user-notification.component';
import { UserNotificationService } from 'app/entities/user-notification/user-notification.service';
import { UserNotification } from 'app/shared/model/user-notification.model';

describe('Component Tests', () => {
    describe('UserNotification Management Component', () => {
        let comp: UserNotificationComponent;
        let fixture: ComponentFixture<UserNotificationComponent>;
        let service: UserNotificationService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [UserNotificationComponent],
                providers: []
            })
                .overrideTemplate(UserNotificationComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UserNotificationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserNotificationService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new UserNotification(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.userNotifications[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
