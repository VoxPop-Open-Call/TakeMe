/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { UserMessagingTokenComponent } from 'app/entities/user-messaging-token/user-messaging-token.component';
import { UserMessagingTokenService } from 'app/entities/user-messaging-token/user-messaging-token.service';
import { UserMessagingToken } from 'app/shared/model/user-messaging-token.model';

describe('Component Tests', () => {
    describe('UserMessagingToken Management Component', () => {
        let comp: UserMessagingTokenComponent;
        let fixture: ComponentFixture<UserMessagingTokenComponent>;
        let service: UserMessagingTokenService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [UserMessagingTokenComponent],
                providers: []
            })
                .overrideTemplate(UserMessagingTokenComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UserMessagingTokenComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserMessagingTokenService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new UserMessagingToken(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.userMessagingTokens[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
