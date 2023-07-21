import {Component, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-famility-tutors-change-status-dialog',
    templateUrl: './tutor-change-status-dialog.component.html'
})
export class TutorChangeStatusDialogComponent {
    @Input()
    name;

    @Input()
    newStatus;

    constructor(
        public activeModal: NgbActiveModal
    ) {
    }
}
