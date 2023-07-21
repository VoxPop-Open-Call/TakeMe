import { Location } from 'app/shared/model//location.model';
import { Child } from 'app/shared/model//child.model';
import { IStopAuditEvent, StopAuditEvent } from 'app/shared/model//stop-audit-event.model';
import { Moment } from 'moment';

export enum StopPointType {
    COLLECTION = 'COLLECTION',
    DELIVER = 'DELIVER'
}

export interface IStopPoint {
    id?: number;
    stopPointType?: StopPointType;
    scheduledTime?: Moment;
    estimatedArriveTime?: Moment;
    effectiveArriveTime?: Moment;
    location?: Location;
    childList?: Child[];
    stopAuditEvents?: StopAuditEvent[];
}

export class StopPoint implements IStopPoint {
    constructor(
        public id?: number,
        public stopPointType?: StopPointType,
        public scheduledTime?: Moment,
        public estimatedArriveTime?: Moment,
        public effectiveArriveTime?: Moment,
        public location?: Location,
        public childList?: Child[],
        public stopAuditEvents?: StopAuditEvent[]
    ) {}
}
