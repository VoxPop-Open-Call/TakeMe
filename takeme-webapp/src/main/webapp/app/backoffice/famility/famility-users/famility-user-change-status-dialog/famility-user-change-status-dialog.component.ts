import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-famility-user-change-status-dialog',
    templateUrl: './famility-user-change-status-dialog.component.html',
    styles: []
})
export class FamilityUserChangeStatusDialogComponent implements OnInit {
    @Input()
    activate: boolean;

    constructor(private activeModal: NgbActiveModal) {}

    ngOnInit() {}

    close() {
        this.activeModal.close();
    }

    dismiss() {
        this.activeModal.dismiss();
    }
}
