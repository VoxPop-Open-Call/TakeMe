/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ChildSubscriptionDetailComponent } from 'app/entities/child-subscription/child-subscription-detail.component';
import { ChildSubscription } from 'app/shared/model/child-subscription.model';

describe('Component Tests', () => {
    describe('ChildSubscription Management Detail Component', () => {
        let comp: ChildSubscriptionDetailComponent;
        let fixture: ComponentFixture<ChildSubscriptionDetailComponent>;
        const route = ({ data: of({ childSubscription: new ChildSubscription(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ChildSubscriptionDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ChildSubscriptionDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ChildSubscriptionDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.childSubscription).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
