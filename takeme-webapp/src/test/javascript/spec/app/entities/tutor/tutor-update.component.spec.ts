/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { TutorUpdateComponent } from 'app/entities/tutor/tutor-update.component';
import { TutorService } from 'app/entities/tutor/tutor.service';
import { Tutor } from 'app/shared/model/tutor.model';

describe('Component Tests', () => {
    describe('Tutor Management Update Component', () => {
        let comp: TutorUpdateComponent;
        let fixture: ComponentFixture<TutorUpdateComponent>;
        let service: TutorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [TutorUpdateComponent]
            })
                .overrideTemplate(TutorUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TutorUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TutorService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Tutor(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.tutor = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Tutor();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.tutor = entity;
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
