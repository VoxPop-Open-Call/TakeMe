import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AlertController } from '@ionic/angular';
import { AppVersionService } from 'src/app/shared/services/app-version.service';

@Component({
  selector: 'app-about',
  templateUrl: './about.page.html',
  styleUrls: ['./about.page.scss'],
})
export class AboutPage implements OnInit {

    constructor(
        private router: Router,
        public alertController: AlertController,
        private route: ActivatedRoute,
        private appVersionService: AppVersionService
    ) {}

    fromInformation: string;
    goTo: string;
    appVersion;

    ngOnInit() {
        this.getVersionNumber();
        this.route.params.subscribe(param => {
            this.fromInformation = param.from;
            switch (this.fromInformation) {

                case 'settings': {
                        this.goTo = 'settings';
                        break;
                    }
                case 'itineraries': {
                        this.goTo = 'itineraries';
                        break;
                    }
            }
        });

    }

    private getVersionNumber() {
        this.appVersionService.getVersionNumber().then(
            versionNumber => this.appVersion = versionNumber,
            error => this.appVersion = 'teste'
        );
    }

    goToContacts() {
        this.router.navigate(['/app/settings/about/contacts', {
            from: this.goTo
        }]);
    }

    goToTermsAndConditions() {
        this.router.navigate(['/app/settings/about/terms-and-conditions', {
            from: this.goTo
        }]);
    }

    goToFaq() {
        this.router.navigate(['/app/settings/about/faq', {
            from: this.goTo
        }]);
    }

    goToSettings() {
        this.router.navigateByUrl('/app/tabs/settings');
    }

}
