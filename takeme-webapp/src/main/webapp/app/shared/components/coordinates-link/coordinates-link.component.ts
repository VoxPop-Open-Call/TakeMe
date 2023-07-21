import {Component, Input, OnInit} from '@angular/core';

@Component({
    selector: 'jhi-coordinates-link',
    templateUrl: './coordinates-link.component.html'
})
export class CoordinatesLinkComponent implements OnInit {
    @Input()
    latitude: string;

    @Input()
    longitude: string;

    constructor() {
    }

    ngOnInit() {
    }
}
