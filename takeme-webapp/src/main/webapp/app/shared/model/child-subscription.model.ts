import { Moment } from 'moment';
import { IChild } from 'app/shared/model//child.model';
import { IOrganization } from 'app/shared/model//organization.model';

export const enum StatusType {
    NEW = 'NEW',
    ACTIVE = 'ACTIVE',
    INACTIVE = 'INACTIVE',
    PENDING = 'PENDING'
}

export interface IChildSubscription {
    id?: number;
    statusType?: StatusType;
    famility?: boolean;
    subscriptionDate?: Moment;
    activationDate?: Moment;
    comments?: string;
    child?: IChild;
    organization?: IOrganization;
}

export class ChildSubscription implements IChildSubscription {
    constructor(
        public id?: number,
        public statusType?: StatusType,
        public famility?: boolean,
        public subscriptionDate?: Moment,
        public activationDate?: Moment,
        public comments?: string,
        public child?: IChild,
        public organization?: IOrganization
    ) {
        this.famility = this.famility || false;
    }
}
