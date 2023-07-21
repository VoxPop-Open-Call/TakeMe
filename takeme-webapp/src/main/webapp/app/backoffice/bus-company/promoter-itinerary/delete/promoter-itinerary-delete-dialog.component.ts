import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPromoterItinerary } from '../promoter-itinerary.model';
import { OperatorPromoterItineraryService } from '../service/promoter-itinerary.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './promoter-itinerary-delete-dialog.component.html',
})
export class PromoterItineraryDeleteDialogComponent {
  promoterItinerary?: IPromoterItinerary;

  constructor(protected promoterItineraryService: OperatorPromoterItineraryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.promoterItineraryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
