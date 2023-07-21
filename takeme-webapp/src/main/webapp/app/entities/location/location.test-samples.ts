import { LocationType } from 'app/entities/enumerations/location-type.model';

import { ILocation, NewLocation } from './location.model';

export const sampleWithRequiredData: ILocation = {
  id: 91847,
  designation: 'parsing',
  street: 'Olson View',
  portNumber: 'Lead monetize indigo',
  postalCode: 'International Computer',
  city: 'North Sonyafort',
  country: 'India',
  type: LocationType['PRIVATE'],
  longitude: 'mission-critical solution',
  latitude: 'Wooden Oklahoma',
  plusCode: 'Tools',
};

export const sampleWithPartialData: ILocation = {
  id: 25255,
  designation: 'bypass',
  street: 'Koelpin Island',
  portNumber: 'Upgradable',
  postalCode: 'JBOD redundant',
  city: 'Yakima',
  country: 'Sri Lanka',
  type: LocationType['PRIVATE'],
  longitude: 'Branch Fish invoice',
  latitude: 'Applications',
  plusCode: 'up Metal Avon',
};

export const sampleWithFullData: ILocation = {
  id: 21507,
  designation: 'Consultant',
  street: 'Josiah Prairie',
  portNumber: 'Shoes Jewelery',
  floor: 'digital ivory solid',
  postalCode: 'Specialist',
  city: 'Conroyport',
  country: 'Panama',
  type: LocationType['SCHOOL'],
  longitude: 'generate',
  latitude: 'Viaduct generation',
  plusCode: 'haptic withdrawal SCSI',
};

export const sampleWithNewData: NewLocation = {
  designation: 'end-to-end HTTP',
  street: 'Jacobi Course',
  portNumber: 'withdrawal',
  postalCode: 'redundant product',
  city: 'Alvenahaven',
  country: 'Zimbabwe',
  type: LocationType['PRIVATE'],
  longitude: 'Wooden content bypassing',
  latitude: 'task-force Rustic',
  plusCode: 'withdrawal',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
