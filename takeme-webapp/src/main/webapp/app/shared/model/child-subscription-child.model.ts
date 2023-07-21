import { IOrganization, StatusType } from './organization.model';

export interface IChildSubscriptionChild {
    id?: number;
    organizationPhone?: string;
    organization?: IOrganization;
    comments?: string;
    statusType?: StatusType;
}

export class ChildSubscriptionChild implements IChildSubscriptionChild {
    constructor(
        public id?: number,
        public organizationPhone?: string,
        public organization?: IOrganization,
        public comments?: string,
        public statusType?: StatusType
    ) {}
}
