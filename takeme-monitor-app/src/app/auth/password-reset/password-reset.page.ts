import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {PasswordResetService} from '../../shared/services/password-reset.service';
import {Logger} from '../../shared/providers/logger-provider.service';
import {NgForm} from '@angular/forms';


@Component({
    selector: 'app-password-reset',
    templateUrl: './password-reset.page.html',
    styleUrls: ['./password-reset.page.scss'],
})
export class PasswordResetPage {

    constructor(
        private router: Router,
        private passwordResetService: PasswordResetService,
        private logger: Logger
    ) {
    }

    requestReset(form: NgForm) {
        this.passwordResetService.reset(form.value.email).subscribe(() => {
            this.logger.debug('Successfully requested the password to reset. Navigating to CheckEmail page.');
            this.router.navigateByUrl('/auth/password-reset/check-email');
        });
    }

}
