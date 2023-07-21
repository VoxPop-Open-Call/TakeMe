/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ServiceStopPointDetailComponent } from 'app/entities/service-stop-point/service-stop-point-detail.component';
import { ServiceStopPoint } from 'app/shared/model/service-stop-point.model';

describe('Component Tests', () => {
    describe('ServiceStopPoint Management Detail Component', () => {
        let comp: ServiceStopPointDetailComponent;
        let fixture: ComponentFixture<ServiceStopPointDetailComponent>;
        const route = ({ data: of({ serviceStopPoint: new ServiceStopPoint(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ServiceStopPointDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ServiceStopPointDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ServiceStopPointDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.serviceStopPoint).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
