import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-users-dialog-change-status',
    templateUrl: './users-dialog-change-status.component.html'
})
export class UsersDialogChangeStatusComponent {
    @Input()
    name: string;
    @Input()
    activated: boolean;

    constructor(public activeModal: NgbActiveModal) {}
}
