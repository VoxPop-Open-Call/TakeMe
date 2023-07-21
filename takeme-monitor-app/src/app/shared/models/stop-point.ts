import { Location } from './location';
import { Itinerary } from './itinerary';
import { Child } from './child';
import { StopPointType } from '../enums/stop-point-type.enum';
import { StopAuditEvent } from './stop-audit-event';

export class StopPoint {

    id?: string;
    stopPointType?: StopPointType;
    scheduledTime?: string;
    estimatedArriveTime?: string;
    effectiveArriveTime?: string;
    location?: Location;
    itinerary?: Itinerary;
    childList?: Child[];
    stopAuditEvents?: StopAuditEvent[];
}
