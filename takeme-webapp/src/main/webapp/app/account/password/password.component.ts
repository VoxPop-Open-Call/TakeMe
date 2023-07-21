import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Principal } from 'app/core';
import { PasswordService } from './password.service';
import { LoginService } from 'app/core';

@Component({
    selector: 'jhi-password',
    templateUrl: './password.component.html',
    providers: [PasswordService]
})
export class PasswordComponent implements OnInit {
    doNotMatch: string;
    error: string;
    success: string;
    account: any;
    currentPassword: string;
    newPassword: string;
    confirmPassword: string;

    constructor(
        private router: Router,
        private passwordService: PasswordService,
        private principal: Principal,
        private loginService: LoginService
    ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
    }

    changePassword() {
        if (this.newPassword !== this.confirmPassword) {
            this.error = null;
            this.success = null;
            this.doNotMatch = 'ERROR';
        } else {
            this.doNotMatch = null;
            this.passwordService.save(this.newPassword, this.currentPassword).subscribe({
                next: () => {
                    this.error = null;
                    this.success = 'OK';
                    this.loginService.logout();
                    this.router.navigate(['']);
                },
                error: () => {
                    this.success = null;
                    this.error = 'ERROR';
                }
            });
        }
    }
}
