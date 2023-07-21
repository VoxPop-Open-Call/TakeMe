import {Component, OnInit, Renderer2, ElementRef} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE} from 'app/shared';
import {Register} from './register.service';
import {TranslateService} from "@ngx-translate/core";
import {Router} from "@angular/router";
import {ITermsAndConditions} from "../../shared/model/terms-and-conditions";

@Component({
  selector: 'jhi-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent implements OnInit {
  confirmPassword: string;
  doNotMatch: string;
  error: string;
  errorEmailExists: string;
  errorUserExists: string;
  registerAccount: any;
  success: boolean;
  termsCheckbox = false;
  termsAndConditionsURL;

  constructor(
    private languageService: TranslateService,
    private registerService: Register,
    private elementRef: ElementRef,
    private renderer: Renderer2,
    private router: Router
  ) {
  }

  ngOnInit() {
    this.fetchTermsAndConditionsURL();
    this.success = false;
    this.registerAccount = {};
  }

  private fetchTermsAndConditionsURL() {
    this.registerService
        .getTermsAndConditionsURL()
        .subscribe({
          next: (response: ITermsAndConditions) => this.termsAndConditionsURL = response.termsAndConditionsURL,
          error: response => this.processError(response)
        });
  }

  register() {
    if (this.registerAccount.password !== this.confirmPassword) {
      this.doNotMatch = 'ERROR';
    } else {
      this.doNotMatch = null;
      this.error = null;
      this.errorUserExists = null;
      this.errorEmailExists = null;
      this.registerAccount.langKey = this.languageService.currentLang;
      this.registerService.save({
        firstName: this.registerAccount.firstName,
        lastName: this.registerAccount.lastName,
        password: this.registerAccount.password,
        email: this.registerAccount.email,
        type: "TUTOR"
      }).subscribe({
        next: () => {
          this.success = true;
        },
        error: response => this.processError(response)
      });
    }
  }

  back() {
    this.router.navigate(['../']);
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = 'ERROR';
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = 'ERROR';
    } else {
      this.error = 'ERROR';
    }
  }

  readTerms() {
    this.termsCheckbox = !this.termsCheckbox;
  }
}
