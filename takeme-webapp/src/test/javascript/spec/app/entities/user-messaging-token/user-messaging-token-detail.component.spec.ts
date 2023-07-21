/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { UserMessagingTokenDetailComponent } from 'app/entities/user-messaging-token/user-messaging-token-detail.component';
import { UserMessagingToken } from 'app/shared/model/user-messaging-token.model';

describe('Component Tests', () => {
    describe('UserMessagingToken Management Detail Component', () => {
        let comp: UserMessagingTokenDetailComponent;
        let fixture: ComponentFixture<UserMessagingTokenDetailComponent>;
        const route = ({ data: of({ userMessagingToken: new UserMessagingToken(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [UserMessagingTokenDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(UserMessagingTokenDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserMessagingTokenDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.userMessagingToken).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
