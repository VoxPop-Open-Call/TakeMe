import { Component, OnInit } from '@angular/core';

import { Principal, AccountService, JhiLanguageHelper } from 'app/core';
import { TranslateService } from "@ngx-translate/core";

@Component({
    selector: 'jhi-settings',
    templateUrl: './settings.component.html'
})
export class SettingsComponent implements OnInit {
    error: string;
    success: string;
    settingsAccount: any;
    languages: any[];

    constructor(
        private account: AccountService,
        private principal: Principal,
        private languageService: TranslateService,
        private languageHelper: JhiLanguageHelper
    ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.settingsAccount = this.copyAccount(account);
        });
        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
        });
    }

    save() {
        this.account.save(this.settingsAccount).subscribe(
            () => {
                this.error = null;
                this.success = 'OK';
                this.principal.identity(true).then(account => {
                    this.settingsAccount = this.copyAccount(account);
                });
                if (this.settingsAccount.langKey !== this.languageService.currentLang) {
                    this.languageService.use(this.settingsAccount.langKey);
                }
            },
            () => {
                this.success = null;
                this.error = 'ERROR';
            }
        );
    }

    copyAccount(account) {
        return {
            activated: account.activated,
            email: account.email,
            firstName: account.firstName,
            langKey: account.langKey,
            lastName: account.lastName,
            login: account.login,
            imageUrl: account.imageUrl
        };
    }
}
