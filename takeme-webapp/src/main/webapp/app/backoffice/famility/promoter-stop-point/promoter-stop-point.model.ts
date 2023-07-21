import dayjs from 'dayjs/esm';
import { ILocation } from 'app/entities/location/location.model';

export interface IPromoterStopPoint {
  id: number;
  name?: string | null;
  scheduledTime?: dayjs.Dayjs | null;
  promoterItineraryId?: number;
  location?: Pick<ILocation, 'id' | 'street'> | null;
}

export type NewPromoterStopPoint = Omit<IPromoterStopPoint, 'id'> & { id: null };
