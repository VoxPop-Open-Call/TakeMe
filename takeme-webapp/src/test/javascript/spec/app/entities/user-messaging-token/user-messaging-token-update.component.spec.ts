/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { UserMessagingTokenUpdateComponent } from 'app/entities/user-messaging-token/user-messaging-token-update.component';
import { UserMessagingTokenService } from 'app/entities/user-messaging-token/user-messaging-token.service';
import { UserMessagingToken } from 'app/shared/model/user-messaging-token.model';

describe('Component Tests', () => {
    describe('UserMessagingToken Management Update Component', () => {
        let comp: UserMessagingTokenUpdateComponent;
        let fixture: ComponentFixture<UserMessagingTokenUpdateComponent>;
        let service: UserMessagingTokenService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [UserMessagingTokenUpdateComponent]
            })
                .overrideTemplate(UserMessagingTokenUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UserMessagingTokenUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserMessagingTokenService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new UserMessagingToken(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.userMessagingToken = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new UserMessagingToken();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.userMessagingToken = entity;
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
