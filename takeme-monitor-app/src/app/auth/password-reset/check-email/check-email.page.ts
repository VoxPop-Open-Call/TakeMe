import {Component} from '@angular/core';
import {Router} from '@angular/router';

@Component({
    selector: 'app-check-email',
    templateUrl: './check-email.page.html',
    styleUrls: ['./check-email.page.scss'],
})
export class CheckEmailPage {

    constructor(
        private router: Router
    ) {
    }

}
