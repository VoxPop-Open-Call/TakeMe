import { Organization } from 'app/shared/model/organization.model';
import { Driver } from 'app/shared/model/driver.model';
import { Moment } from 'moment';

export class Account {
    constructor(
        public id?: number,
        public activated?: boolean,
        public statusChangeDate?: Moment,
        public authorities?: string[],
        public email?: string,
        public firstName?: string,
        public langKey?: string,
        public lastName?: string,
        public login?: string,
        public imageUrl?: string,
        public organizationId?: string,
        public organization?: Organization,
        public driver?: Driver,
        public driverId?: number
    ) {}
}
