import {Component, Input} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'jhi-bus-company-passengers-detail-comment-dialog',
    templateUrl: './comment-dialog.component.html'
})
export class CommentDialogComponent {
    @Input()
    comment;

    constructor(
        private activeModal: NgbActiveModal
    ) {
    }

    close() {
        this.activeModal.close();
    }
}
