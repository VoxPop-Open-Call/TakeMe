import { Component, Input, OnInit } from '@angular/core';
import { DayOfWeek } from 'app/shared/model/day-of-week';

@Component({
    selector: 'jhi-days-of-week',
    templateUrl: './days-of-week.component.html',
    styles: []
})
export class DaysOfWeekComponent implements OnInit {
    @Input()
    daysOfWeekInput: any[];
    daysOfWeekDisplay = new Map<number, DayOfWeek>();
    daysOfWeekNames = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];

    constructor() {}

    ngOnInit() {
        for (let i = 1; i <= 7; i++) {
            const dayOfWeekItem = new DayOfWeek(i, this.daysOfWeekNames[i - 1], false);
            this.daysOfWeekDisplay.set(i, dayOfWeekItem);
        }
        this.daysOfWeekInput.forEach(dayOfWeekInput => {
            this.daysOfWeekDisplay.get(dayOfWeekInput.id).activate();
        });
    }
}
