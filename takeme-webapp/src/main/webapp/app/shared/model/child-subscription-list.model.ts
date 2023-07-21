import { Moment } from 'moment';

export enum ChildSubscriptionType {
    ACTIVE = 'active',
    INACTIVE = 'inactive',
    PENDING = 'pending'
}

export interface IChildSubscriptionList {
    id?: number;
    userName?: string;
    userNif?: string;
    userId?: number;
    userPhotoId?: number;
    userPhoto?: string;
    childName?: string;
    childNif?: string;
    childId?: number;
    childPhotoId?: number;
    childPhoto?: string;
    childDateOfBirth?: Moment;
    famility?: boolean;
    subscriptionDate?: Moment;
    activationDate?: Moment;
    deactivationDate?: Moment;
    additionalInformation?: String;
    comments?: string;
    cardNumber?: string;
}

export class ChildSubscriptionListModel implements IChildSubscriptionList {
    constructor(
        public id?: number,
        public userName?: string,
        public userNif?: string,
        public userId?: number,
        public userPhotoId?: number,
        public userPhoto?: string,
        public childName?: string,
        public childNif?: string,
        public childId?: number,
        public childPhotoId?: number,
        public childPhoto?: string,
        public childDateOfBirth?: Moment,
        public famility?: boolean,
        public subscriptionDate?: Moment,
        public activationDate?: Moment,
        public deactivationDate?: Moment,
        public additionalInformation?: String,
        public comments?: string,
        public cardNumber?: string
    ) {}
}
