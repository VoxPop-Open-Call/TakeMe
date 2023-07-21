import { Injectable } from '@angular/core';
import { UserService } from './user.service';
import { UserProvider } from '../providers/user-provider.service';
import { Role } from '../enums/role.enum';

@Injectable({
    providedIn: 'root'
})
export class InitializerService {

    constructor(private userService: UserService,
                private userProvider: UserProvider) {
    }

    initialize() {
        return new Promise((resolve, reject) => {
            this.userService.getUserInformation().subscribe((userInfo) => {
                const driverRole = userInfo.body.authorities.filter(role => role === Role.ROLE_BUS_COMPANY_DRIVER);
                if (userInfo.body.driverId !== null && driverRole) {
                    this.userProvider.setUserInfo(userInfo.body);
                }
                resolve();
            }, (error) => {
                resolve();
            });
        });
    }
}
