export interface IDayOfWeek {
    weekdayId?: number;
    weekdayName?: string;
    active?: boolean;
}

export class DayOfWeek implements IDayOfWeek {
    constructor(public weekdayId?: number, public weekdayName?: string, public active?: boolean) {}

    activate() {
        this.active = true;
    }
}
