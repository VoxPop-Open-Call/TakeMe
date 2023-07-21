import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-error-dialog',
    templateUrl: './error-dialog.component.html',
    styles: []
})
export class ErrorDialogComponent {
    @Input()
    message: string;

    constructor(private activeModal: NgbActiveModal) {}

    dismiss() {
        this.activeModal.dismiss();
    }
}
