import { Moment } from 'moment';
import { IStopPoint } from 'app/shared/model//stop-point.model';
import { Child } from 'app/shared/model/child.model';

export const enum StopEventType {
    CHECK_IN = 'CHECK_IN',
    CHECK_OUT = 'CHECK_OUT',
    CANCELED = 'CANCELED'
}

export interface IStopAuditEvent {
    id?: number;
    eventTime?: Moment;
    eventType?: StopEventType;
    stopPoint?: IStopPoint;
    child?: Child;
}

export class StopAuditEvent implements IStopAuditEvent {
    constructor(
        public id?: number,
        public eventTime?: Moment,
        public eventType?: StopEventType,
        public stopPoint?: IStopPoint,
        public child?: Child
    ) {}
}
