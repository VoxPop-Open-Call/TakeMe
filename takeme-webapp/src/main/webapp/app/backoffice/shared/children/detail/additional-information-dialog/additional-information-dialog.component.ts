import {Component, Input} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'jhi-bus-company-passengers-detail-additional-information-dialog',
    templateUrl: './additional-information-dialog.component.html'
})
export class AdditionalInformationDialogComponent {
    @Input()
    additionalInformation;

    constructor(
        private activeModal: NgbActiveModal
    ) {
    }

    close() {
        this.activeModal.close();
    }
}
