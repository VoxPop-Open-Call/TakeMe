import dayjs from 'dayjs/esm';

import { IPromoterItinerary, NewPromoterItinerary } from './promoter-itinerary.model';

export const sampleWithRequiredData: IPromoterItinerary = {
  id: 30751,
  name: 'Fish sky',
  startDate: dayjs('2023-05-22'),
};

export const sampleWithPartialData: IPromoterItinerary = {
  id: 77394,
  name: 'Marketing Music Frozen',
  startDate: dayjs('2023-05-23'),
  endDate: dayjs('2023-05-22'),
};

export const sampleWithFullData: IPromoterItinerary = {
  id: 93607,
  name: 'Horizontal Chips',
  startDate: dayjs('2023-05-23'),
  endDate: dayjs('2023-05-23'),
};

export const sampleWithNewData: NewPromoterItinerary = {
  name: 'leverage cross-platform',
  startDate: dayjs('2023-05-23'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
