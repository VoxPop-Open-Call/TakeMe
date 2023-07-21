import { Injectable } from '@angular/core';
import { SessionStorageService } from 'ngx-webstorage';
import { Observable, Subject } from 'rxjs';
import { AccountService } from './account.service';
import { TranslateService } from "@ngx-translate/core";
import { Tutor } from "../../shared/model/tutor.model";

@Injectable({ providedIn: 'root' })
export class Principal {
    private userIdentity: any;
    private authenticated = false;
    private authenticationState = new Subject<any>();
    private idOrganization = null;

    constructor(
        private languageService: TranslateService,
        private sessionStorage: SessionStorageService,
        private account: AccountService
    ) {}

    authenticate(identity) {
        this.userIdentity = identity;
        this.authenticated = identity !== null;
        this.authenticationState.next(this.userIdentity);
    }

    hasAnyAuthority(authorities: string[]): Promise<boolean> {
        return Promise.resolve(this.hasAnyAuthorityDirect(authorities));
    }

    hasAnyAuthorityDirect(authorities: string[]): boolean {
        if (!this.authenticated || !this.userIdentity || !this.userIdentity.authorities) {
            return false;
        }

        for (let i = 0; i < authorities.length; i++) {
            if (this.userIdentity.authorities.includes(authorities[i])) {
                return true;
            }
        }

        return false;
    }

    hasAuthority(authority: string): Promise<boolean> {
        if (!this.authenticated) {
            return Promise.resolve(false);
        }

        return this.identity().then(
            id => {
                return Promise.resolve(id.authorities && id.authorities.includes(authority));
            },
            () => {
                return Promise.resolve(false);
            }
        );
    }

    identity(force?: boolean): Promise<any> {
        if (force === true) {
            this.userIdentity = undefined;
        }

        // check and see if we have retrieved the userIdentity data from the server.
        // if we have, reuse it by immediately resolving
        if (this.userIdentity) {
            return Promise.resolve(this.userIdentity);
        }

        // retrieve the userIdentity data from the server, update the identity object, and then resolve.
        return this.account
            .getAlt()
            .toPromise()
            .then(response => {
                const account = response.body;
                if (account) {
                    this.userIdentity = account;
                    this.authenticated = true;
                    this.idOrganization = account.organizationId;

                    // After retrieve the account info, the language will be changed to
                    // the user's preferred language configured in the account setting
                    const langKey = this.sessionStorage.retrieve('locale') || this.userIdentity.langKey;
                    this.languageService.use(langKey);
                } else {
                    this.userIdentity = null;
                    this.authenticated = false;
                    this.idOrganization = null;
                }
                this.authenticationState.next(this.userIdentity);
                return this.userIdentity;
            })
            .catch(err => {
                this.userIdentity = null;
                this.authenticated = false;
                this.idOrganization = null;
                this.authenticationState.next(this.userIdentity);
                return null;
            });
    }

    isAuthenticated(): boolean {
        // if it's not authenticated, return false
        if (!this.authenticated) {
          return false;
        }

        // if it's authenticated, check if it's a tutor
        // if it's not, return true
        if (!this.userIdentity.authorities.includes('ROLE_TUTOR')) {
          return true;
        }

        // if it's a tutor, check if it's created (i.e. tutor not null)
        // if it's not null, then registration was already done, so return true
        // if it's null, then registration has to be done, so return false
        return this.userIdentity.tutor != null;
    }

    isIdentityResolved(): boolean {
        return this.userIdentity !== undefined;
    }

    getAuthenticationState(): Observable<any> {
        return this.authenticationState.asObservable();
    }

    getImageUrl(): string {
        return this.isIdentityResolved() ? this.userIdentity.imageUrl : null;
    }

    getUserIdentity(): any {
        return this.userIdentity;
    }

    getIdOrganization(): string {
        return this.idOrganization;
    }

    setTutor(tutor: Tutor) {
        this.userIdentity.tutor = tutor;
    }
}
