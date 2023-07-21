import { StopPoint } from './stop-point';
import { Child } from './child';
import { Itinerary } from './itinerary';
import { StopEventType } from '../enums/stop-event-type.enum';

export class StopAuditEvent {

    id: number;
    stopEventTime?: string;
    eventType: StopEventType;
    // stopPoint?: StopPoint;
    child?: Child;
    // itinerary?: Itinerary;

}
