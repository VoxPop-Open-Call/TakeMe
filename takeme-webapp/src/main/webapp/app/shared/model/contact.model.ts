import { IOrganization } from 'app/shared/model//organization.model';

export interface IContact {
    id?: number;
    name?: string;
    phoneNumber?: string;
    email?: string;
    publicData?: boolean;
    organization?: IOrganization;
}

export class Contact implements IContact {
    constructor(
        public id?: number,
        public name?: string,
        public phoneNumber?: string,
        public email?: string,
        public publicData?: boolean,
        public organization?: IOrganization
    ) {
        this.publicData = this.publicData || false;
    }
}
