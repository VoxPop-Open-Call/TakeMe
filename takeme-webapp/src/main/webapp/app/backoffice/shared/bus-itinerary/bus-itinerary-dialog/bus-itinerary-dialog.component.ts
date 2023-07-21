import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-bus-itinerary-dialog',
    templateUrl: './bus-itinerary-dialog.component.html',
    styles: []
})
export class BusItineraryDialogComponent implements OnInit {
    @Input()
    actionType: string;

    constructor(private activeModal: NgbActiveModal) {}

    ngOnInit() {}

    dismiss() {
        this.activeModal.dismiss();
    }

    delete() {
        this.activeModal.close();
    }
}
