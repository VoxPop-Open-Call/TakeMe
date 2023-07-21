/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { IdentificationCardTypeDetailComponent } from 'app/entities/identification-card-type/identification-card-type-detail.component';
import { IdentificationCardType } from 'app/shared/model/identification-card-type.model';

describe('Component Tests', () => {
    describe('IdentificationCardType Management Detail Component', () => {
        let comp: IdentificationCardTypeDetailComponent;
        let fixture: ComponentFixture<IdentificationCardTypeDetailComponent>;
        const route = ({ data: of({ identificationCardType: new IdentificationCardType(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [IdentificationCardTypeDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(IdentificationCardTypeDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(IdentificationCardTypeDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.identificationCardType).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
