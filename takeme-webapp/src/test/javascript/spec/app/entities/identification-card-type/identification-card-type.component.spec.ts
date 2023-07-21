/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { IdentificationCardTypeComponent } from 'app/entities/identification-card-type/identification-card-type.component';
import { IdentificationCardTypeService } from 'app/entities/identification-card-type/identification-card-type.service';
import { IdentificationCardType } from 'app/shared/model/identification-card-type.model';

describe('Component Tests', () => {
    describe('IdentificationCardType Management Component', () => {
        let comp: IdentificationCardTypeComponent;
        let fixture: ComponentFixture<IdentificationCardTypeComponent>;
        let service: IdentificationCardTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [IdentificationCardTypeComponent],
                providers: []
            })
                .overrideTemplate(IdentificationCardTypeComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(IdentificationCardTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IdentificationCardTypeService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new IdentificationCardType(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.identificationCardTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
