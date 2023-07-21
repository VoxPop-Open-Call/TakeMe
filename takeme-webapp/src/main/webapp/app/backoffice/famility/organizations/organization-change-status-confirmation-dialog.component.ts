import {Component, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-organization-change-status-confirmation-dialog',
    templateUrl: './organization-change-status-confirmation-dialog.component.html'
})
export class OrganizationChangeStatusConfirmationDialogComponent {
    @Input()
    name;

    @Input()
    newStatus;

    constructor(
        public activeModal: NgbActiveModal
    ) {
    }
}
