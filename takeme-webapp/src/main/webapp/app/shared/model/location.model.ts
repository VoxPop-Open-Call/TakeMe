import { ITutor } from 'app/shared/model//tutor.model';
import { IOrganization } from 'app/shared/model//organization.model';

export enum LocationType {
    PRIVATE = 'PRIVATE',
    SCHOOL = 'SCHOOL',
    SCHOOL_EXTERNAL = 'SCHOOL_EXTERNAL'
}

export interface ILocation {
    id?: number;
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
    tutors?: ITutor[];
    organizations?: IOrganization[];
}

export class Location implements ILocation {
    constructor(
        public id?: number,
        public designation?: string,
        public street?: string,
        public portNumber?: string,
        public floor?: string,
        public postalCode?: string,
        public city?: string,
        public country?: string,
        public type?: LocationType,
        public longitude?: string,
        public latitude?: string,
        public plusCode?: string,
        public tutors?: ITutor[],
        public organizations?: IOrganization[]
    ) {
    }
}
