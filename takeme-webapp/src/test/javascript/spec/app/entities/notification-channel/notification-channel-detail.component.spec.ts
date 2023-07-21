/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { NotificationChannelDetailComponent } from 'app/entities/notification-channel/notification-channel-detail.component';
import { NotificationChannel } from 'app/shared/model/notification-channel.model';

describe('Component Tests', () => {
    describe('NotificationChannel Management Detail Component', () => {
        let comp: NotificationChannelDetailComponent;
        let fixture: ComponentFixture<NotificationChannelDetailComponent>;
        const route = ({ data: of({ notificationChannel: new NotificationChannel(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [NotificationChannelDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(NotificationChannelDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(NotificationChannelDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.notificationChannel).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
