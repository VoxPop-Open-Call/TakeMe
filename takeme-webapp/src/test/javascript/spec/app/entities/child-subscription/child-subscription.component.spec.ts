/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { FamilityBackofficeTestModule } from '../../../test.module';
import { ChildSubscriptionComponent } from 'app/entities/child-subscription/child-subscription.component';
import { ChildSubscriptionService } from 'app/entities/child-subscription/child-subscription.service';
import { ChildSubscription } from 'app/shared/model/child-subscription.model';

describe('Component Tests', () => {
    describe('ChildSubscription Management Component', () => {
        let comp: ChildSubscriptionComponent;
        let fixture: ComponentFixture<ChildSubscriptionComponent>;
        let service: ChildSubscriptionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FamilityBackofficeTestModule],
                declarations: [ChildSubscriptionComponent],
                providers: []
            })
                .overrideTemplate(ChildSubscriptionComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ChildSubscriptionComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ChildSubscriptionService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new ChildSubscription(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.childSubscriptions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
