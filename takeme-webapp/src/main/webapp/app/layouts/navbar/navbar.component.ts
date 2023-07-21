import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SessionStorageService } from 'ngx-webstorage';

import { VERSION } from 'app/app.constants';
import { JhiLanguageHelper, Principal, LoginModalService, LoginService } from 'app/core';
import { ProfileService } from '../profiles/profile.service';
import { Organization } from 'app/shared/model/organization.model';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { TranslateService } from "@ngx-translate/core";
import { OrganizationService } from "app/entities/organization/organization.service";

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
    inProduction?: boolean;
    isNavbarCollapsed = true;
    languages: any[];
    openAPIEnabled?: boolean;
    modalRef: NgbModalRef;
    version = '';
    organization: Organization;
    photo = '';
    account: any;

    constructor(
        private loginService: LoginService,
        private translateService: TranslateService,
        private languageHelper: JhiLanguageHelper,
        private sessionStorageService: SessionStorageService,
        private principal: Principal,
        private loginModalService: LoginModalService,
        private profileService: ProfileService,
        private router: Router,
        private organizationService: OrganizationService
    ) {
        if (VERSION) {
          this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
        }
    }

    ngOnInit(): void {
        this.loadOrganization();

        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
        });

        this.profileService.getProfileInfo().subscribe(profileInfo => {
          this.inProduction = profileInfo.inProduction;
          this.openAPIEnabled = profileInfo.openAPIEnabled;
        });

        this.principal.identity().then(account => {
            this.account = account;
        });
    }

    loadOrganization(): void {
        this.organizationService.find(+this.principal.getIdOrganization()).subscribe(
            (result: HttpResponse<Organization>) => {
                this.organization = result.body;
                this.loadPhotoOrganization();
            },
            (error: HttpErrorResponse) => {
                console.log('Error getting organization information: ' + error.message);
            }
        );
    }

    loadPhotoOrganization(): void {
        if (this.organization.photoId) {
            this.organizationService.getPhoto(this.organization.id, this.organization.photoId).subscribe(
                (result: HttpResponse<any>) => {
                    this.photo = 'data:image/jpg;base64,' + result.body.photo;
                },
                (error: HttpErrorResponse) => {
                    console.log('Error loading photo organization: ' + error.message);
                }
            );
        }
    }

    changeLanguage(languageKey: string): void {
        this.sessionStorageService.store('locale', languageKey);
        this.translateService.use(languageKey);
    }

    collapseNavbar(): void {
        this.isNavbarCollapsed = true;
    }

    isAuthenticated(): boolean {
        return this.principal.isAuthenticated();
    }

    login(): void {
        this.modalRef = this.loginModalService.open();
    }

    logout(): void {
        this.collapseNavbar();
        this.loginService.logout();
        this.router.navigate(['']);
    }

    toggleNavbar(): void {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }

    getImageUrl(): string {
        return this.isAuthenticated() ? this.principal.getImageUrl() : null;
    }
}
