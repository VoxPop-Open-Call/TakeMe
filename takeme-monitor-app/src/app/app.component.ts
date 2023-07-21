import { Component } from '@angular/core';

import { Platform } from '@ionic/angular';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
import { StatusBar } from '@ionic-native/status-bar/ngx';

import { Storage } from '@ionic/storage';
import { Router } from '@angular/router';
import { UserProvider } from './shared/providers/user-provider.service';
import { TranslateService } from '@ngx-translate/core';
import { DriverService } from './shared/services/driver.service';
import { Logger } from './shared/providers/logger-provider.service';
import { CurrentItineraryProvider } from './shared/providers/current-itinerary-provider.service';

@Component({
    selector: 'app-root',
    templateUrl: 'app.component.html'
})
export class AppComponent {
    constructor(
        private platform: Platform,
        private splashScreen: SplashScreen,
        private statusBar: StatusBar,
        private storage: Storage,
        private router: Router,
        private userProvider: UserProvider,
        private currentItineraryProvider: CurrentItineraryProvider,
        private translate: TranslateService,
        private driverService: DriverService,
        private logger: Logger,
    ) {
        this.initializeApp();
    }

    initializeApp() {
        this.platform.ready().then(() => {
            this.translate.setDefaultLang('pt');
            this.translate.use('pt');

            this.statusBar.styleDefault();
            this.splashScreen.hide();
            if (this.userProvider.userDTO && this.userProvider.userDTO.driverId && this.userProvider.getOrganizationType() === 'BUS_COMPANY') {
                let currentItinerary;
                this.driverService.getCurrentItinerary(this.userProvider.getDriverId()).subscribe((currentItineraryResponse) => {
                    if (currentItineraryResponse.status === 204) {
                        this.logger.debug('Received a code 204 response from backend, which means that there is no current itinerary for the monitor.' +
                            ' Navigating to Itinerary List page...');
                        this.router.navigateByUrl('/app');
                    } else {
                        this.logger.debug('Received a current itinerary from backend. Stashing it in a provider.' +
                            ' Navigating to Current Itinerary page...');
                        currentItinerary = currentItineraryResponse.body;
                        this.currentItineraryProvider.setCurrentItinerary(currentItinerary);
                        this.router.navigateByUrl('/app/current-itinerary');
                    }
                });
            } else {
                this.router.navigateByUrl('');
            }
        });
    }
}
