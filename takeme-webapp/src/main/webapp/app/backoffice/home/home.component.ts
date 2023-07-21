import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Principal } from '../../core';
import { Authority } from "../../config/authority.constants";

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styles: []
})
export class HomeComponent implements OnInit {
    isFamility = false;
    isBusCompany = false;
    isTutor = false;

    constructor(private principal: Principal,
                private router: Router
    ) {}

    ngOnInit() {
        this.principal.hasAuthority(Authority.PROMOTER).then(result => {
            this.isFamility = result;

            if (result) {
              this.router.navigate(['promoter/services']);
            }
        });

        this.principal.hasAuthority(Authority.OPERATOR).then(result => {
            this.isBusCompany = result;

            if (result) {
              this.router.navigate(['operator/subscriptions']);
            }
        });

        this.principal.hasAuthority(Authority.TUTOR).then(result => {
            this.isTutor = result;

            if (result) {
              this.router.navigate(['tutor/passengers']);
            }
        });
    }
}
