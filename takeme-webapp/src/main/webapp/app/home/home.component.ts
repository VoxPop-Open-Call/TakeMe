import {Component, OnInit} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {LoginModalService, Principal, Account} from 'app/core';
import {EventManager} from "../core/util/event-manager.service";
import {Router} from "@angular/router";

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;

    languages: string[] = ["PT-PT", "EN", "PT-BR"];
    languageSelected: string;

    constructor(
        private loginModalService: LoginModalService,
        private router: Router,
        private principal: Principal,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.principal.identity().then(account => this.account = account);
        this.eventManager.subscribe('authenticationSuccess', () => this.principal.identity().then(account => this.account = account));
        this.languageSelected = this.languages[0];
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    changeLanguage(languageIndex: number) {
        this.languageSelected = this.languages[languageIndex];
    }
}
