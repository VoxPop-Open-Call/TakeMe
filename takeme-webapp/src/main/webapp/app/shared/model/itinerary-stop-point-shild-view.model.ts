import { ILocation } from "./location.model";
import { ItineraryStatusType } from "./itinerary.model";
import dayjs from "dayjs/esm";

export interface IItineraryStopPointChildView {
    childId?: number;
    itineraryId?: number;
    childName?: string;
    itineraryName?: string;
    collectionScheduledTime?: dayjs.Dayjs;
    collectionLocation?: ILocation;
    deliverScheduledTime?: dayjs.Dayjs;
    deliverLocation?: ILocation;
    itineraryStatusType?: ItineraryStatusType;
}

export class ItineraryStopPointChildView implements IItineraryStopPointChildView {
    constructor(
        public childId?: number,
        public itineraryId?: number,
        public childName?: string,
        public itineraryName?: string,
        public collectionScheduledTime?: dayjs.Dayjs,
        public collectionLocation?: ILocation,
        public deliverScheduledTime?: dayjs.Dayjs,
        public deliverLocation?: ILocation,
        public itineraryStatusType?: ItineraryStatusType
    ) {}
}
