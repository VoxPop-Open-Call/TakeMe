import dayjs from 'dayjs/esm';

import { IPromoterStopPoint, NewPromoterStopPoint } from './promoter-stop-point.model';

export const sampleWithRequiredData: IPromoterStopPoint = {
  id: 67195,
  name: 'generation HTTP Future',
  scheduledTime: dayjs('2023-05-23T10:39'),
};

export const sampleWithPartialData: IPromoterStopPoint = {
  id: 81720,
  name: 'Shoes Handmade District',
  scheduledTime: dayjs('2023-05-22T21:10'),
};

export const sampleWithFullData: IPromoterStopPoint = {
  id: 92628,
  name: 'Implemented CSS',
  scheduledTime: dayjs('2023-05-23T04:03'),
};

export const sampleWithNewData: NewPromoterStopPoint = {
  name: 'responsive Specialist Chief',
  scheduledTime: dayjs('2023-05-22T13:15'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
