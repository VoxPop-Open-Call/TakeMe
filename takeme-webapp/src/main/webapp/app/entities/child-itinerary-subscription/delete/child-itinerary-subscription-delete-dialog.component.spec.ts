jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ChildItinerarySubscriptionService } from '../service/child-itinerary-subscription.service';

import { ChildItinerarySubscriptionDeleteDialogComponent } from './child-itinerary-subscription-delete-dialog.component';

describe('ChildItinerarySubscription Management Delete Component', () => {
  let comp: ChildItinerarySubscriptionDeleteDialogComponent;
  let fixture: ComponentFixture<ChildItinerarySubscriptionDeleteDialogComponent>;
  let service: ChildItinerarySubscriptionService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ChildItinerarySubscriptionDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(ChildItinerarySubscriptionDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ChildItinerarySubscriptionDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ChildItinerarySubscriptionService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
