import { Component, Input } from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { StatusType } from "../../../../../shared/model/child.model";

@Component({
    selector: 'jhi-bus-company-subscriptions-list-subscription-dialog',
    templateUrl: './subscription-dialog.component.html'
})
export class SubscriptionDialogComponent {
    @Input()
    status;

    additionalInformation;
    comments;

    activeSubscription = StatusType.ACTIVE;
    inactiveSubscription = StatusType.INACTIVE;

    constructor(
        private activeModal: NgbActiveModal
    ) {}

    dismiss() {
        this.activeModal.dismiss();
    }

    close() {
        this.activeModal.close({
            additionalInformation: this.additionalInformation,
            comments: this.comments
        });
    }
}
