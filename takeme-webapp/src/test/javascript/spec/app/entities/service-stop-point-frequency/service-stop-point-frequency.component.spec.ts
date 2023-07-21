/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ServiceStopPointFrequencyComponent } from 'app/entities/service-stop-point-frequency/service-stop-point-frequency.component';
import { ServiceStopPointFrequencyService } from 'app/entities/service-stop-point-frequency/service-stop-point-frequency.service';
import { ServiceStopPointFrequency } from 'app/shared/model/service-stop-point-frequency.model';

describe('Component Tests', () => {
    describe('ServiceStopPointFrequency Management Component', () => {
        let comp: ServiceStopPointFrequencyComponent;
        let fixture: ComponentFixture<ServiceStopPointFrequencyComponent>;
        let service: ServiceStopPointFrequencyService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ServiceStopPointFrequencyComponent],
                providers: []
            })
                .overrideTemplate(ServiceStopPointFrequencyComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ServiceStopPointFrequencyComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ServiceStopPointFrequencyService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new ServiceStopPointFrequency(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.serviceStopPointFrequencies[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
