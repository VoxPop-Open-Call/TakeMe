import { StatusType } from '../enums/status-type.enum';
import { Location } from './location';
import { Contact } from './contact';
import { OrganizationType } from '../enums/organization-type.enum';

export class Organization {
    id?: Number;
    name?: String;
    nifCountry?: String;
    nifNumber?: String;
    organizationType?: OrganizationType;
    statusType?: StatusType;
    location?: Location;
    photoId?: Number;
    photo?: String;
    contacts?: Contact[];


}
