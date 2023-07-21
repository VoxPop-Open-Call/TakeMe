import { Injectable } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import { SessionStorageService, LocalStorageService } from 'ngx-webstorage';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class FirebaseAuthenticationService {
    private jwtService = new JwtHelperService();

    constructor(
        private afAuth: AngularFireAuth,
        private localStorageService: LocalStorageService,
        private sessionStorageService: SessionStorageService
    ) {}

    login(email: string, password: string) {
        return this.afAuth.signInWithEmailAndPassword(email, password);
    }

    getAuthToken() {
        return this.afAuth.idToken;
    }

    getAccessToken(): Promise<string> {
        return new Promise((resolve, reject) => {
            const currentToken = this.sessionStorageService.retrieve('token');
            if (currentToken) {
                if (this.isValid(currentToken)) {
                    resolve(currentToken);
                } else {
                    if (this.afAuth.currentUser) {
                        this.afAuth.currentUser.then(user => {
                          if(user) {
                            user.getIdToken().then(newToken => {
                              this.sessionStorageService.store('token', newToken);
                              resolve(newToken);
                            });
                          }
                        })
                    }
                    else {
                      resolve(null);
                    }
                }
            } else {
                if (this.afAuth.currentUser) {
                    this.afAuth.currentUser.then(user => {
                      if(user) {
                        user.getIdToken().then(newToken => {
                          this.sessionStorageService.store('token', newToken);
                          resolve(newToken);
                        });
                      } else {
                        resolve(null);
                      }
                    })
                } else {
                    resolve(null);
                }
            }
        });
    }

    // For the future with remember me
    storeAuthenticationToken(jwt, rememberMe) {
        if (rememberMe) {
            this.localStorageService.store('token', jwt);
        } else {
            this.sessionStorageService.store('token', jwt);
        }
    }

    logout(): Observable<any> {
        return new Observable(observer => {
            this.afAuth.signOut().then();
            this.localStorageService.clear('token');
            this.sessionStorageService.clear('token');
            observer.complete();
        });
    }

    getEmailVerification(): Promise<boolean> {
        return new Promise((resolve) => {
            this.afAuth.currentUser.then(user => {
                resolve(user.emailVerified);
            });
        })
    }

    private isValid(token: string) {
        return !this.jwtService.isTokenExpired(token);
    }
}
