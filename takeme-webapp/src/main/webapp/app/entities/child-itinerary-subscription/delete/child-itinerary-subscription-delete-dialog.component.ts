import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChildItinerarySubscription } from '../child-itinerary-subscription.model';
import { ChildItinerarySubscriptionService } from '../service/child-itinerary-subscription.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './child-itinerary-subscription-delete-dialog.component.html',
})
export class ChildItinerarySubscriptionDeleteDialogComponent {
  childItinerarySubscription?: IChildItinerarySubscription;

  constructor(protected childItinerarySubscriptionService: ChildItinerarySubscriptionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.childItinerarySubscriptionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
