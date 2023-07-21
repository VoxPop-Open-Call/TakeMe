import { LocationType } from '../enums/location-type.enum';

export class Location {

    id?: string;
    designation?: string;
    street?: string;
    portNumber?: string;
    floor?: string;
    postalCode?: string;
    city?: string;
    country?: string;
    type?: LocationType;
    longitude?: string;
    latitude?: string;
    plusCode?: string;
}
