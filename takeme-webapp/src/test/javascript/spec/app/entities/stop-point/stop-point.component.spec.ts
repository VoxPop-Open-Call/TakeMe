/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { StopPointComponent } from 'app/entities/stop-point/stop-point.component';
import { StopPointService } from 'app/entities/stop-point/stop-point.service';
import { StopPoint } from 'app/shared/model/stop-point.model';

describe('Component Tests', () => {
    describe('StopPoint Management Component', () => {
        let comp: StopPointComponent;
        let fixture: ComponentFixture<StopPointComponent>;
        let service: StopPointService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [StopPointComponent],
                providers: []
            })
                .overrideTemplate(StopPointComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(StopPointComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StopPointService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new StopPoint(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.stopPoints[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
