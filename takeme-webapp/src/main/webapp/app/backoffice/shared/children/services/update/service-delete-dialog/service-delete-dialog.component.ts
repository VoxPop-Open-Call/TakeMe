import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-bus-company-passengers-services-update-service-delete-detail',
    templateUrl: './service-delete-dialog.component.html'
})
export class ServiceDeleteDialog implements OnInit {
    constructor(
        private activeModal: NgbActiveModal
    ) {
    }

    ngOnInit() {
    }

    dismiss() {
        this.activeModal.dismiss();
    }

    delete() {
        this.activeModal.close();
    }
}
