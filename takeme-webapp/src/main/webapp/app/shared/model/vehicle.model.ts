import { IOrganization } from 'app/shared/model//organization.model';

export interface IVehicle {
    id?: number;
    designation?: string;
    licensePlate?: string;
    capacity?: number;
    organization?: IOrganization;
}

export class Vehicle implements IVehicle {
    constructor(
        public id?: number,
        public designation?: string,
        public licensePlate?: string,
        public capacity?: number,
        public organization?: IOrganization
    ) {}
}
