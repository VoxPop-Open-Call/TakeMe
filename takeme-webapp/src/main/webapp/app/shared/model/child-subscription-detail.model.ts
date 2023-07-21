import { IChild } from './child.model';
import { ITutor } from './tutor.model';
import { Moment } from 'moment';

export interface IChildSubscriptionDetail {
    id?: string;
    tutor?: ITutor;
    child?: IChild;
    statusType?: string;
    comments?: string;
    additionalInformation?: string;
    famility?: boolean;
    subscriptionDate?: Moment;
    activationDate?: Moment;
    deactivationDate?: Moment;
    cardNumber?: string;
}

export class ChildSubscriptionDetail implements IChildSubscriptionDetail {
    constructor(
        public id?: string,
        public tutor?: ITutor,
        public child?: IChild,
        public statusType?: string,
        public comments?: string,
        public additionalInformation?: string,
        public famility?: boolean,
        public subscriptionDate?: Moment,
        public activationDate?: Moment,
        public deactivationDate?: Moment,
        public cardNumber?: string
    ) {}
}
