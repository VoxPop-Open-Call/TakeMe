import dayjs from 'dayjs/esm';

import { StatusType } from 'app/entities/enumerations/status-type.model';

import { IChildItinerarySubscription, NewChildItinerarySubscription } from './child-itinerary-subscription.model';

export const sampleWithRequiredData: IChildItinerarySubscription = {
  id: 40372,
  statusType: StatusType['NEW'],
  subscriptionDate: dayjs('2023-05-28T13:11'),
  activationDate: dayjs('2023-05-29T03:18'),
};

export const sampleWithPartialData: IChildItinerarySubscription = {
  id: 89085,
  statusType: StatusType['ACTIVE'],
  subscriptionDate: dayjs('2023-05-28T13:53'),
  activationDate: dayjs('2023-05-29T06:54'),
  additionalInformation: 'Coordinator markets strategize',
};

export const sampleWithFullData: IChildItinerarySubscription = {
  id: 26531,
  statusType: StatusType['NEW'],
  subscriptionDate: dayjs('2023-05-29T09:52'),
  activationDate: dayjs('2023-05-28T14:14'),
  deactivationDate: dayjs('2023-05-29T01:00'),
  comments: 'Tools online',
  additionalInformation: 'Creative Polarised',
  cardNumber: 'Savings Keyboard neural-net',
};

export const sampleWithNewData: NewChildItinerarySubscription = {
  statusType: StatusType['ACTIVE'],
  subscriptionDate: dayjs('2023-05-28T22:21'),
  activationDate: dayjs('2023-05-29T11:27'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
