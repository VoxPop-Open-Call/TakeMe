/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { StopPointDetailComponent } from 'app/entities/stop-point/stop-point-detail.component';
import { StopPoint } from 'app/shared/model/stop-point.model';

describe('Component Tests', () => {
    describe('StopPoint Management Detail Component', () => {
        let comp: StopPointDetailComponent;
        let fixture: ComponentFixture<StopPointDetailComponent>;
        const route = ({ data: of({ stopPoint: new StopPoint(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [StopPointDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(StopPointDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(StopPointDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.stopPoint).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
