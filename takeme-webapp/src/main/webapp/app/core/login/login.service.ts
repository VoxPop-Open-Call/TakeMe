import {Injectable} from '@angular/core';

import {Principal} from '../auth/principal.service';
import {FirebaseAuthenticationService} from '../auth/firebase-authentication.service';
import {AccountDTO} from "../../shared/model/account-dto";

@Injectable({providedIn: 'root'})
export class LoginService {
  constructor(
    private principal: Principal,
    private firebaseAuthenticationService: FirebaseAuthenticationService
  ) {
  }

  login(credentials, callback?): Promise<AccountDTO> {
    const cb = callback || function () {};

    return new Promise((resolve, reject) => {
      this.firebaseAuthenticationService.login(credentials.username.valueOf(), credentials.password.valueOf()).then(
        userInfo => {
          this.principal.identity(true).then(account => {
            resolve(account);
          });
          return cb();
        },
        err => {
          this.logout();
          reject(err);
          return cb(err);
        }
      );
    });
  }

  loginWithToken(jwt, rememberMe) {
    // return this.authServerProvider.loginWithToken(jwt, rememberMe);
  }

  logout() {
    this.firebaseAuthenticationService.logout().subscribe(
      () => {},
      () => {},
      () => {
        this.principal.authenticate(null);
      }
    );
  }

  isAuthenticated() {
    return this.principal.isAuthenticated();
  }

  isVerified() {
    return this.firebaseAuthenticationService.getEmailVerification();
  }
}
