import { AfterViewInit, Component, ElementRef, OnInit, Renderer2 } from '@angular/core';
import { EMAIL_NOT_FOUND_TYPE } from 'app/shared';
import { PasswordResetInitService } from './password-reset-init.service';

@Component({
    selector: 'jhi-password-reset-init',
    templateUrl: './password-reset-init.component.html',
    providers: [PasswordResetInitService]
})
export class PasswordResetInitComponent implements OnInit, AfterViewInit {
    error: string;
    errorEmailNotExists: string;
    resetAccount: any;
    success: string;

    constructor(private passwordResetInitService: PasswordResetInitService, private elementRef: ElementRef, private renderer: Renderer2) {}

    ngOnInit() {
        this.resetAccount = {};
    }

    ngAfterViewInit() {
      this.renderer.selectRootElement(this.elementRef.nativeElement.querySelector('#email'), true).focus();
    }

    requestReset() {
        this.error = null;
        this.errorEmailNotExists = null;

        // trocar service
        this.passwordResetInitService.reset(this.resetAccount.email).subscribe({
            next: () => {
                this.success = 'OK';
            },
            error: response => {
                this.success = null;
                if (response.status === 400 && response.error.type === EMAIL_NOT_FOUND_TYPE) {
                    this.errorEmailNotExists = 'ERROR';
                } else {
                    this.error = 'ERROR';
                }
            }
        });
    }
}
