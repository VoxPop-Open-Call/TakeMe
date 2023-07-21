/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ServiceStopPointFrequencyDetailComponent } from 'app/entities/service-stop-point-frequency/service-stop-point-frequency-detail.component';
import { ServiceStopPointFrequency } from 'app/shared/model/service-stop-point-frequency.model';

describe('Component Tests', () => {
    describe('ServiceStopPointFrequency Management Detail Component', () => {
        let comp: ServiceStopPointFrequencyDetailComponent;
        let fixture: ComponentFixture<ServiceStopPointFrequencyDetailComponent>;
        const route = ({ data: of({ serviceStopPointFrequency: new ServiceStopPointFrequency(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ServiceStopPointFrequencyDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ServiceStopPointFrequencyDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ServiceStopPointFrequencyDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.serviceStopPointFrequency).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
