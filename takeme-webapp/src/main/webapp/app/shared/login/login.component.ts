import {Component, AfterViewInit, Renderer2, ElementRef} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {Router} from '@angular/router';

import {LoginService} from 'app/core/login/login.service';
import {StateStorageService} from 'app/core/auth/state-storage.service';
import {EventManager} from "../../core/util/event-manager.service";

@Component({
  selector: 'jhi-login-modal',
  templateUrl: './login.component.html'
})
export class JhiLoginModalComponent implements AfterViewInit {
  authenticationError: boolean;
  tutorVerificationError: boolean;
  password: string;
  rememberMe: boolean;
  username: string;
  credentials: any;

  constructor(
    private eventManager: EventManager,
    private loginService: LoginService,
    private stateStorageService: StateStorageService,
    private elementRef: ElementRef,
    private renderer: Renderer2,
    private router: Router,
    public activeModal: NgbActiveModal
  ) {
    this.credentials = {};
  }

  ngAfterViewInit() {
    setTimeout(() => this.renderer.selectRootElement(this.elementRef.nativeElement.querySelector('#username'), true).focus(), 0);
  }

  cancel() {
    this.credentials = {
      username: null,
      password: null,
      rememberMe: true
    };
    this.authenticationError = false;
    this.tutorVerificationError = false;
    this.activeModal.dismiss('cancel');
  }

  login() {
    this.loginService
      .login({
        username: this.username,
        password: this.password,
        rememberMe: this.rememberMe
      })
      .then(accountInfo => {
        if (accountInfo.authorities.includes('ROLE_TUTOR') && accountInfo.tutor == null) {
          this.loginService.isVerified().then(isEmailVerified => {
            if (isEmailVerified) {
              this.activeModal.dismiss();
              this.router.navigate(['/finish-registration']);
            } else {
              this.authenticationError = false;
              this.tutorVerificationError = true;
              this.loginService.logout();
            }
          })
        } else {
          this.authenticationError = false;
          this.tutorVerificationError = false;
          this.activeModal.dismiss('login success');

          this.eventManager.broadcast({
            name: 'authenticationSuccess',
            content: 'Sending Authentication Success'
          });

          this.router.navigate(['famility']);
        }
      })
      .catch(() => {
        this.authenticationError = true;
        this.tutorVerificationError = false;
      });
  }

  register() {
    this.activeModal.dismiss('to state register');
    this.router.navigate(['/register']);
  }

  requestResetPassword() {
    this.activeModal.dismiss('to state requestReset');
    this.router.navigate(['/reset-password']);
  }
}
