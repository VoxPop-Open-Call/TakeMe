import { ILocation } from "app/shared/model//location.model";
import { IContact } from "./contact.model";

export enum OrganizationType {
    BUS_COMPANY = 'BUS_COMPANY',
    FAMILITY = 'FAMILITY',
    TUTOR = 'TUTOR'
}

export enum StatusType {
    NEW = 'NEW',
    ACTIVE = 'ACTIVE',
    INACTIVE = 'INACTIVE',
    PENDING = 'PENDING'
}

export interface IOrganization {
    id?: number;
    name?: string;
    nifCountry?: string;
    nifNumber?: string;
    organizationType?: OrganizationType;
    statusType?: StatusType;
    lastModifiedDate?: Date;
    location?: ILocation;
    contacts?: IContact[];
    photoId?: number;
}

export class Organization implements IOrganization {
    constructor(
        public id?: number,
        public name?: string,
        public nifCountry?: string,
        public nifNumber?: string,
        public organizationType?: OrganizationType,
        public statusType?: StatusType,
        public lastModifiedDate?: Date,
        public location?: ILocation,
        public contacts?: IContact[],
        public photoId?: number,
    ) {}
}
