import { Moment } from 'moment';
import { IOrganization } from 'app/shared/model//organization.model';
import { IVehicle } from 'app/shared/model//vehicle.model';
import { IDriver } from 'app/shared/model//driver.model';
import { Location } from 'app/shared/model/location.model';
import { ItineraryStopPoint } from 'app/shared/model/itinerary-stop-point.model';
import {IPromoterItinerary} from "../../backoffice/famility/promoter-itinerary/promoter-itinerary.model";

export const enum ItineraryStatusType {
    READY_TO_START = 'READY_TO_START',
    IN_PROGRESS = 'IN_PROGRESS',
    FINISHED = 'FINISHED',
    CANCELED = 'CANCELED'
}

export interface IItinerary {
    id?: number;
    name?: string;
    scheduledTime?: Moment;
    effectiveStartTime?: Moment;
    effectiveEndTime?: Moment;
    estimatedStartLocation?: Location;
    itineraryStatusType?: ItineraryStatusType;
    estimatedTotalTime?: number;
    estimatedKM?: number;
    organization?: IOrganization;
    vehicle?: IVehicle;
    driver?: IDriver;
    ocupation?: number;
    itineraryStopPointList?: ItineraryStopPoint[];
    promoterItinerary?: IPromoterItinerary;
}

export class Itinerary implements IItinerary {
    constructor(
        public id?: number,
        public name?: string,
        public scheduledTime?: Moment,
        public effectiveStartTime?: Moment,
        public effectiveEndTime?: Moment,
        public estimatedStartLocation?: Location,
        public itineraryStatusType?: ItineraryStatusType,
        public estimatedTotalTime?: number,
        public estimatedKM?: number,
        public organization?: IOrganization,
        public vehicle?: IVehicle,
        public driver?: IDriver,
        public ocupation?: number,
        public itineraryStopPointList?: ItineraryStopPoint[],
        public promoterItinerary?: IPromoterItinerary
    ) {}
}
