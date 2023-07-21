import {IIdentificationCardType} from 'app/shared/model//identification-card-type.model';
import {ILocation} from 'app/shared/model//location.model';
import {IChild} from 'app/shared/model//child.model';
import {IUser} from 'app/core';
import {Moment} from 'moment';

export const enum StatusType {
    NEW = 'NEW',
    ACTIVE = 'ACTIVE',
    INACTIVE = 'INACTIVE',
    PENDING = 'PENDING'
}

export interface ITutor {
    id?: number;
    userId?: string;
    user?: IUser;
    name?: string;
    nifCountry?: string;
    nifNumber?: string;
    identificationCardTypeName?: string;
    identificationCardNumber?: string;
    phoneNumber?: string;
    famility?: boolean;
    statusType?: StatusType;
    photoId?: string;
    photo?: string;
    createdBy?: string;
    createdDate?: Moment;
    lastModifiedBy?: string;
    lastModifiedDate?: Moment;
    identificationCardType?: IIdentificationCardType;
    locations?: ILocation[];
    children?: IChild[];
}

export class Tutor implements ITutor {
    constructor(
        public id?: number,
        public userId?: string,
        public user?: IUser,
        public name?: string,
        public nifCountry?: string,
        public nifNumber?: string,
        public identificationCardTypeName?: string,
        public identificationCardNumber?: string,
        public phoneNumber?: string,
        public famility?: boolean,
        public statusType?: StatusType,
        public photoId?: string,
        public photo?: string,
        public createdBy?: string,
        public createdDate?: Moment,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Moment,
        public identificationCardType?: IIdentificationCardType,
        public locations?: ILocation[],
        public children?: IChild[]
    ) {
        this.famility = this.famility || false;
    }
}
