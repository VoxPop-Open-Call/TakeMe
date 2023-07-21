/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ServiceStopPointDaysOfWeekComponent } from 'app/entities/service-stop-point-days-of-week/service-stop-point-days-of-week.component';
import { ServiceStopPointDaysOfWeekService } from 'app/entities/service-stop-point-days-of-week/service-stop-point-days-of-week.service';
import { ServiceStopPointDaysOfWeek } from 'app/shared/model/service-stop-point-days-of-week.model';

describe('Component Tests', () => {
    describe('ServiceStopPointDaysOfWeek Management Component', () => {
        let comp: ServiceStopPointDaysOfWeekComponent;
        let fixture: ComponentFixture<ServiceStopPointDaysOfWeekComponent>;
        let service: ServiceStopPointDaysOfWeekService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ServiceStopPointDaysOfWeekComponent],
                providers: []
            })
                .overrideTemplate(ServiceStopPointDaysOfWeekComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ServiceStopPointDaysOfWeekComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ServiceStopPointDaysOfWeekService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new ServiceStopPointDaysOfWeek(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.serviceStopPointDaysOfWeeks[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
