/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { NotificationChannelUserDetailComponent } from 'app/entities/notification-channel-user/notification-channel-user-detail.component';
import { NotificationChannelUser } from 'app/shared/model/notification-channel-user.model';

describe('Component Tests', () => {
    describe('NotificationChannelUser Management Detail Component', () => {
        let comp: NotificationChannelUserDetailComponent;
        let fixture: ComponentFixture<NotificationChannelUserDetailComponent>;
        const route = ({ data: of({ notificationChannelUser: new NotificationChannelUser(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [NotificationChannelUserDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(NotificationChannelUserDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(NotificationChannelUserDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.notificationChannelUser).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
