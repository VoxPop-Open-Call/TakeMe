import { Moment } from 'moment';
import { StopPoint } from 'app/shared/model/stop-point.model';
import { ItineraryStatusType } from 'app/shared/model/itinerary.model';

export enum ItineraryStopPointType {
    COLLECTION = 'COLLECTION',
    DELIVER = 'DELIVER'
}

export class ItineraryStopPoint {
    constructor(
        public id?: number,
        public estimatedTime?: Moment,
        public order?: number,
        public stopPoint?: StopPoint,
        public itineraryId?: number,
        public itineraryStatusType?: ItineraryStatusType
    ) {}
}
