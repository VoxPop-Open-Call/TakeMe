import {Moment} from 'moment';
import {IServiceStopPointFrequency} from 'app/shared/model//service-stop-point-frequency.model';
import {IServiceStopPointDaysOfWeek} from 'app/shared/model//service-stop-point-days-of-week.model';
import {ILocation} from 'app/shared/model/location.model';

export enum StopPointType {
    COLLECTION = 'COLLECTION',
    DELIVER = 'DELIVER'
}

export const enum StatusType {
    NEW = 'NEW',
    ACTIVE = 'ACTIVE',
    INACTIVE = 'INACTIVE',
    PENDING = 'PENDING'
}

export interface IServiceStopPoint {
    id?: number;
    stopPointType?: StopPointType;
    startHour?: string;
    combinedTime?: string;
    startFrequencyDate?: Moment;
    statusType?: StatusType;
    frequency?: IServiceStopPointFrequency;
    location?: ILocation;
    serviceId?: number;
    serviceStopPointDaysOfWeeks?: IServiceStopPointDaysOfWeek[];
    match?: boolean;
}

export class ServiceStopPoint implements IServiceStopPoint {
    constructor(
        public id?: number,
        public stopPointType?: StopPointType,
        public startHour?: string,
        public combinedTime?: string,
        public startFrequencyDate?: Moment,
        public statusType?: StatusType,
        public frequency?: IServiceStopPointFrequency,
        public location?: ILocation,
        public serviceId?: number,
        public serviceStopPointDaysOfWeeks?: IServiceStopPointDaysOfWeek[],
        public match?: boolean
    ) {
    }
}
