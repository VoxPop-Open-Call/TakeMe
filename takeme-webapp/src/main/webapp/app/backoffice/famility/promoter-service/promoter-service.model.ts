import dayjs from 'dayjs/esm';
import {IPromoterItinerary} from "../promoter-itinerary/promoter-itinerary.model";

export const enum TransportType {
    CAR = 'CAR',
    BUS = 'BUS',
    WALKING = 'WALKING',
    BICYCLING = 'BICYCLING'
}

export interface IPromoterService {
    id: number;
    name?: string | null;
    logo?: string | null;
    logoContentType?: string | null;
    startDate?: dayjs.Dayjs | null;
    endDate?: dayjs.Dayjs | null;
    needsETA?: boolean | null;
    enrollmentURL?: string | null;
    transportType?: TransportType | null;
    itineraries?: IPromoterItinerary[] | null;
}

export type NewPromoterService = Omit<IPromoterService, 'id'> & { id: null };
