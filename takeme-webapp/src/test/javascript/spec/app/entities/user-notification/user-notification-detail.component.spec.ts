/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { UserNotificationDetailComponent } from 'app/entities/user-notification/user-notification-detail.component';
import { UserNotification } from 'app/shared/model/user-notification.model';

describe('Component Tests', () => {
    describe('UserNotification Management Detail Component', () => {
        let comp: UserNotificationDetailComponent;
        let fixture: ComponentFixture<UserNotificationDetailComponent>;
        const route = ({ data: of({ userNotification: new UserNotification(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [UserNotificationDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(UserNotificationDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserNotificationDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.userNotification).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
