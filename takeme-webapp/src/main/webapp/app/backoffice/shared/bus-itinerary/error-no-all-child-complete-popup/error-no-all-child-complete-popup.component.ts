import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-error-no-all-child-complete-popup',
    templateUrl: './error-no-all-child-complete-popup.component.html',
    styles: []
})
export class ErrorNoAllChildCompletePopupComponent implements OnInit {
    constructor(private activeModal: NgbActiveModal) {}

    ngOnInit() {}

    close() {
        this.activeModal.close();
    }
}
