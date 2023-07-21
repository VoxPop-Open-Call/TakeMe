/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ChildSubscriptionUpdateComponent } from 'app/entities/child-subscription/child-subscription-update.component';
import { ChildSubscriptionService } from 'app/entities/child-subscription/child-subscription.service';
import { ChildSubscription } from 'app/shared/model/child-subscription.model';

describe('Component Tests', () => {
    describe('ChildSubscription Management Update Component', () => {
        let comp: ChildSubscriptionUpdateComponent;
        let fixture: ComponentFixture<ChildSubscriptionUpdateComponent>;
        let service: ChildSubscriptionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ChildSubscriptionUpdateComponent]
            })
                .overrideTemplate(ChildSubscriptionUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ChildSubscriptionUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ChildSubscriptionService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new ChildSubscription(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.childSubscription = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new ChildSubscription();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.childSubscription = entity;
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
