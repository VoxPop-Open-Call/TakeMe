/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { IdentificationCardTypeUpdateComponent } from 'app/entities/identification-card-type/identification-card-type-update.component';
import { IdentificationCardTypeService } from 'app/entities/identification-card-type/identification-card-type.service';
import { IdentificationCardType } from 'app/shared/model/identification-card-type.model';

describe('Component Tests', () => {
    describe('IdentificationCardType Management Update Component', () => {
        let comp: IdentificationCardTypeUpdateComponent;
        let fixture: ComponentFixture<IdentificationCardTypeUpdateComponent>;
        let service: IdentificationCardTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [IdentificationCardTypeUpdateComponent]
            })
                .overrideTemplate(IdentificationCardTypeUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(IdentificationCardTypeUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IdentificationCardTypeService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new IdentificationCardType(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.identificationCardType = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new IdentificationCardType();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.identificationCardType = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
