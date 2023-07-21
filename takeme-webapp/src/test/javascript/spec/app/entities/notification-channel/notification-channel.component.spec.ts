/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { NotificationChannelComponent } from 'app/entities/notification-channel/notification-channel.component';
import { NotificationChannelService } from 'app/entities/notification-channel/notification-channel.service';
import { NotificationChannel } from 'app/shared/model/notification-channel.model';

describe('Component Tests', () => {
    describe('NotificationChannel Management Component', () => {
        let comp: NotificationChannelComponent;
        let fixture: ComponentFixture<NotificationChannelComponent>;
        let service: NotificationChannelService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [NotificationChannelComponent],
                providers: []
            })
                .overrideTemplate(NotificationChannelComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(NotificationChannelComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotificationChannelService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new NotificationChannel(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.notificationChannels[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
