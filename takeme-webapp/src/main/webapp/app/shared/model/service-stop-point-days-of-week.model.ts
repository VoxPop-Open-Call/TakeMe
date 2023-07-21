export const enum DaysOfWeekType {
    SUNDAY = 'SUNDAY',
    MONDAY = 'MONDAY',
    TUESDAY = 'TUESDAY',
    WEDNESDAY = 'WEDNESDAY',
    THURSDAY = 'THURSDAY',
    FRIDAY = 'FRIDAY',
    SATURDAY = 'SATURDAY'
}

export interface IServiceStopPointDaysOfWeek {
    id?: number;
    day?: DaysOfWeekType;
}

export class ServiceStopPointDaysOfWeek implements IServiceStopPointDaysOfWeek {
    constructor(
        public id?: number,
        public day?: DaysOfWeekType
    ) {
    }
}
