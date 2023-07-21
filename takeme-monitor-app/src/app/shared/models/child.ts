import { StatusType } from '../enums/status-type.enum';

export class Child {
    id?: string;
    name?: string;
    nifCountry?: string;
    nifNumber?: string;
    dateOfBirth?: string;
    famility?: boolean;
    statusType?: StatusType;
    photoId: string;
    photo?: string;
}
