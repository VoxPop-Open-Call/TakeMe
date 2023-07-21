import {APP_INITIALIZER, ErrorHandler, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {RouteReuseStrategy} from '@angular/router';

import {IonicModule, IonicRouteStrategy} from '@ionic/angular';
import {SplashScreen} from '@ionic-native/splash-screen/ngx';
import {StatusBar} from '@ionic-native/status-bar/ngx';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {InitializerService} from './shared/services/initializer.service';
import {IonicStorageModule} from '@ionic/storage';
import {AngularFireModule} from '@angular/fire';
import {AngularFireAuth, AngularFireAuthModule} from '@angular/fire/auth';
import {environment} from '../environments/environment';

import {JWT_OPTIONS, JwtModule} from '@auth0/angular-jwt';
import {FirebaseAuthenticationService} from './shared/services/firebase-authentication.service';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {LoggerModule} from 'ngx-logger';
import {TranslateModule, TranslateLoader} from '@ngx-translate/core';
import {GlobalErrorHandler} from './shared/providers/global.error.handler';

import { SharedModule } from './shared/shared.module';
import { LocationLinkModule } from './shared/components/location-link/location-link.module';

@NgModule({
    declarations: [AppComponent],
    entryComponents: [],
    imports: [BrowserModule,
        IonicModule.forRoot(),
        AppRoutingModule,
        AngularFireModule.initializeApp(environment.firebase.config),
        AngularFireAuthModule,
        IonicStorageModule.forRoot(),
        JwtModule.forRoot({
            jwtOptionsProvider: {
                provide: JWT_OPTIONS,
                useFactory: jwtOptionsFactory,
                deps: [FirebaseAuthenticationService]
            }
        }),
        HttpClientModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: (createTranslateLoader),
                deps: [HttpClient]
            }
        }),
        LoggerModule.forRoot(environment.logging),
        SharedModule,
        LocationLinkModule
    ],
    providers: [
        StatusBar,
        SplashScreen,
        AngularFireAuth,
        {provide: RouteReuseStrategy, useClass: IonicRouteStrategy},
        InitializerService,
        {
            provide: APP_INITIALIZER,
            useFactory: (initService: InitializerService) =>
                () => initService.initialize(),
            deps: [InitializerService],
            multi: true
        },
        { provide: ErrorHandler, useClass: GlobalErrorHandler }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}

export function jwtOptionsFactory(tokenService: FirebaseAuthenticationService) {
    /* Header used for authorization */
    const AUTHORIZATION_HEADER = 'X-Authorization-Firebase';

    return {
        tokenGetter: () => {
            return tokenService.getAccessToken();
        },
        whitelistedDomains: environment.WHITELIST_DOMAINS,
        blacklistedRoutes: environment.BLACKLIST_ROUTES,
        headerName: AUTHORIZATION_HEADER,
        authScheme: ''
    };
}

export function createTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
