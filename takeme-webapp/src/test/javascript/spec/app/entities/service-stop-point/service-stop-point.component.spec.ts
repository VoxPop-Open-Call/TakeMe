/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ServiceStopPointComponent } from 'app/entities/service-stop-point/service-stop-point.component';
import { ServiceStopPointService } from 'app/entities/service-stop-point/service-stop-point.service';
import { ServiceStopPoint } from 'app/shared/model/service-stop-point.model';

describe('Component Tests', () => {
    describe('ServiceStopPoint Management Component', () => {
        let comp: ServiceStopPointComponent;
        let fixture: ComponentFixture<ServiceStopPointComponent>;
        let service: ServiceStopPointService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ServiceStopPointComponent],
                providers: []
            })
                .overrideTemplate(ServiceStopPointComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ServiceStopPointComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ServiceStopPointService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new ServiceStopPoint(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.serviceStopPoints[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
