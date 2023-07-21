import { IOrganization } from 'app/shared/model//organization.model';

export interface IDriver {
    id?: number;
    name?: string;
    driverLicense?: string;
    organization?: IOrganization;
    photo?: string;
    photoId?: number;
}

export class Driver implements IDriver {
    constructor(
        public id?: number,
        public name?: string,
        public driverLicense?: string,
        public organization?: IOrganization,
        public photo?: string,
        public photoId?: number
    ) {}
}
