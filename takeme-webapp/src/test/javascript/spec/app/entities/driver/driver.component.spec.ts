/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { DriverComponent } from 'app/entities/driver/driver.component';
import { DriverService } from 'app/entities/driver/driver.service';
import { Driver } from 'app/shared/model/driver.model';

describe('Component Tests', () => {
    describe('Driver Management Component', () => {
        let comp: DriverComponent;
        let fixture: ComponentFixture<DriverComponent>;
        let service: DriverService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [DriverComponent],
                providers: []
            })
                .overrideTemplate(DriverComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DriverComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DriverService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Driver(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.drivers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
