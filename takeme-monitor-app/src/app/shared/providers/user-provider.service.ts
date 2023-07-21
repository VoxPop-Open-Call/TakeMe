import { Injectable } from '@angular/core';
import { UserDTO } from '../models/user-dto';
import { Subject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class UserProvider {

    userDTO: UserDTO;

    userStatusChanged: Subject<UserDTO> = new Subject();

    constructor() {
    }

    setUserInfo(userDTO: UserDTO) {
        this.userDTO = userDTO;
        this.userStatusChanged.next(this.userDTO);
    }

    clearUserInfo() {
        this.userDTO = null;
        this.userStatusChanged.next(this.userDTO);
    }

    getId() {
        return this.userDTO.id;
    }

    getFullName() {
        return this.userDTO.firstName + ' ' + this.userDTO.lastName;
    }

    getEmail() {
        return this.userDTO.email;
    }

    getOrganizationId() {
        return this.userDTO.organizationId;
    }

    getOrganizationType() {
        return this.userDTO.organization.organizationType;
    }

    getDriverId() {
        return this.userDTO.driverId;
    }

    isUserFilled() {
        return this.userDTO != null;
    }

}
