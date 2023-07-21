import {Component, Input} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'jhi-tutor-passengers-list-enrollment-dialog',
    templateUrl: './enrollment-dialog.component.html'
})
export class EnrollmentDialogComponent {
    @Input()
    enrollmentUrl;

    constructor(
        private activeModal: NgbActiveModal
    ) {
    }

    close() {
        this.activeModal.close();
    }
}
