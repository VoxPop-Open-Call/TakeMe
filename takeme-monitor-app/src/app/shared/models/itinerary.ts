import { ItineraryStatusType } from '../enums/itinerary-status-type.enum';
import { Organization } from './organization';
import { Driver } from './driver';
import { Location } from './location';
import { Vehicle } from './vehicle';
import { ItineraryStopPoint } from './itinerary-stop-point';


export class Itinerary {

    id?: string;
    name?: string;
    scheduledTime?: string;
    effectiveStartTime?: string;
    effectiveEndTime?: string;
    itineraryStatusType?: ItineraryStatusType;
    estimatedKM?: string;
    totalTime?: string;
    ocupation?: string;
    estimatedStartLocation?: Location;
    startLocation?: Location;
    endLocation?: Location;
    organization?: Organization;
    vehicle?: Vehicle;
    driver?: Driver;
    itineraryStopPointList?: ItineraryStopPoint[];

}
