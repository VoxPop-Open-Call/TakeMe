import { ItineraryStatusType } from '../enums/itinerary-status-type.enum';

export class ItineraryStatusTypePatchDTO {

    statusType: ItineraryStatusType;
    latitude?: number;
    longitude?: number;

}
