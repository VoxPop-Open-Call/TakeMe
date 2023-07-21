import {Component} from '@angular/core';
import {UserProvider} from '../shared/providers/user-provider.service';
import {FirebaseAuthenticationService} from '../shared/services/firebase-authentication.service';
import {Router} from '@angular/router';
import {Logger} from '../shared/providers/logger-provider.service';

@Component({
    selector: 'app-settings',
    templateUrl: './settings.page.html',
    styleUrls: ['./settings.page.scss'],
})
export class SettingsPage {

    myFullName: string;
    myEmail: string;
    driverPhoto: string;

    constructor(private userProvider: UserProvider,
                private firebaseAuthenticationService: FirebaseAuthenticationService,
                private router: Router,
                private logger: Logger) {
    }

    ionViewWillEnter() {
        this.myFullName = this.userProvider.getFullName();
        this.myEmail = this.userProvider.getEmail();
    }

    logout() {
        this.firebaseAuthenticationService.logout().subscribe(
            (success) => {
                // stop listening here
                // clear other providers here
                this.userProvider.clearUserInfo();
                this.router.navigateByUrl('');
            },
            (error) => this.logger.error('Error logging out.', error)
        );
    }


}
