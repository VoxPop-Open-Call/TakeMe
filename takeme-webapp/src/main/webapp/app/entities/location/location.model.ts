import { ITutor } from 'app/shared/model/tutor.model';
import { IOrganization } from 'app/shared/model/organization.model';
import { LocationType } from 'app/entities/enumerations/location-type.model';

export interface ILocation {
  id: number;
  designation?: string | null;
  street?: string | null;
  portNumber?: string | null;
  floor?: string | null;
  postalCode?: string | null;
  city?: string | null;
  country?: string | null;
  type?: LocationType | null;
  longitude?: string | null;
  latitude?: string | null;
  plusCode?: string | null;
  tutors?: Pick<ITutor, 'id' | 'name'>[] | null;
  organizations?: Pick<IOrganization, 'id' | 'name'>[] | null;
}

export type NewLocation = Omit<ILocation, 'id'> & { id: null };
