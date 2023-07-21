import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPromoterStopPoint } from '../promoter-stop-point.model';
import { PromoterStopPointService } from '../service/promoter-stop-point.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './promoter-stop-point-delete-dialog.component.html',
})
export class PromoterStopPointDeleteDialogComponent {
  promoterStopPoint?: IPromoterStopPoint;

  constructor(protected promoterStopPointService: PromoterStopPointService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.promoterStopPointService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
