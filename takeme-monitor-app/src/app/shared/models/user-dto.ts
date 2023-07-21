import { Organization } from './organization';
import { Role } from '../enums/role.enum';


export class UserDTO {

    id: string;
    firstName: string;
    lastName: string;
    email: string;
    organizationId: string;
    organization: Organization;
    driverId: string;
    authorities: Role[];

}
