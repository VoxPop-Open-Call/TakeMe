/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { NotificationTypeComponent } from 'app/entities/notification-type/notification-type.component';
import { NotificationTypeService } from 'app/entities/notification-type/notification-type.service';
import { NotificationType } from 'app/shared/model/notification-type.model';

describe('Component Tests', () => {
    describe('NotificationType Management Component', () => {
        let comp: NotificationTypeComponent;
        let fixture: ComponentFixture<NotificationTypeComponent>;
        let service: NotificationTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [NotificationTypeComponent],
                providers: []
            })
                .overrideTemplate(NotificationTypeComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(NotificationTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotificationTypeService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new NotificationType(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.notificationTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
