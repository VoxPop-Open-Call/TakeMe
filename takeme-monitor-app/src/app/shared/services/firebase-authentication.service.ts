import {Injectable} from '@angular/core';
import {AngularFireAuth} from '@angular/fire/auth';
import {Observable, of} from 'rxjs';
import {JwtHelperService} from '@auth0/angular-jwt';
import {Logger} from '../providers/logger-provider.service';


@Injectable({
    providedIn: 'root'
})
export class FirebaseAuthenticationService {

    // private jwtService = new JwtHelperService();

    constructor(private angularFireAuth: AngularFireAuth,
                private logger: Logger) {
    }

    login(email: string, password: string) {
        return this.angularFireAuth.auth.signInWithEmailAndPassword(email, password);
    }

    logout(): Observable<any> {
        this.angularFireAuth.auth.signOut().then(() => {
            this.logger.debug('Signed out');
        }).catch((error) => {
            this.logger.error('Error signing out.', error);
        });
        return of({});
    }

    userHasEmailVerified() {
        return this.angularFireAuth.auth.currentUser.emailVerified;
    }

    getAccessToken(): Promise<string> {
        return new Promise((resolve, reject) => {
            this.angularFireAuth.idToken.subscribe((token) => {
                if (token) {
                    resolve(token);
                } else {
                    resolve(null);
                }
            });
        });
    }

    // private isValid(token: string) {
    //     return !this.jwtService.isTokenExpired(token);
    // }

}

