import { StopPoint } from './stop-point';
import { ItineraryStatusType } from '../enums/itinerary-status-type.enum';

export class ItineraryStopPoint {

    id?: string;
    estimatedTime?: string;
    order?: string;
    stopPoint?: StopPoint;
    itineraryId?: string;
    itineraryStatusType?: ItineraryStatusType;
    last = false;
    current = false;
}
