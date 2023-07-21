/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ServiceStopPointDaysOfWeekDetailComponent } from 'app/entities/service-stop-point-days-of-week/service-stop-point-days-of-week-detail.component';
import { ServiceStopPointDaysOfWeek } from 'app/shared/model/service-stop-point-days-of-week.model';

describe('Component Tests', () => {
    describe('ServiceStopPointDaysOfWeek Management Detail Component', () => {
        let comp: ServiceStopPointDaysOfWeekDetailComponent;
        let fixture: ComponentFixture<ServiceStopPointDaysOfWeekDetailComponent>;
        const route = ({ data: of({ serviceStopPointDaysOfWeek: new ServiceStopPointDaysOfWeek(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ServiceStopPointDaysOfWeekDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ServiceStopPointDaysOfWeekDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ServiceStopPointDaysOfWeekDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.serviceStopPointDaysOfWeek).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
