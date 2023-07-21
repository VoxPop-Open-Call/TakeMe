import { LOCALE_ID, NgModule } from '@angular/core';
import { DatePipe, registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Title } from '@angular/platform-browser';
import locale from '@angular/common/locales/en';

import { FirebaseAuthenticationService } from './auth/firebase-authentication.service';
import { AngularFireAuthModule } from '@angular/fire/compat/auth';

import { JWT_OPTIONS, JwtModule } from '@auth0/angular-jwt';
import { Environment, environment } from '../../environments/environment';
import { Angularfire2ConfigModule } from "./providers/angularfire2.provider";

@NgModule({
    imports: [
      HttpClientModule,
      AngularFireAuthModule,
      Angularfire2ConfigModule.initializeApp(environmentFirebase),
        JwtModule.forRoot({
            jwtOptionsProvider: {
                provide: JWT_OPTIONS,
                useFactory: jwtOptionsFactory,
                deps: [FirebaseAuthenticationService, Environment]
            }
        })
    ],
    exports: [],
    declarations: [],
    providers: [
        Title,
        {
            provide: LOCALE_ID,
            useValue: 'en'
        },
        DatePipe
    ]
})
export class FamilityBackofficeCoreModule {
    constructor() {
      registerLocaleData(locale);
    }
}

export function jwtOptionsFactory(tokenService: FirebaseAuthenticationService) {
    return {
        tokenGetter: () => {
            return tokenService.getAccessToken();
        },
        whitelistedDomains: environment.config.whiteListDomains,
        blacklistedRoutes: environment.config.blackListRoutes,
        headerName: environment.config.authorizationHeader,
        authScheme: ''
    };
}

export function environmentFirebase(env: Environment) {
  return environment.config.firebase;
}

