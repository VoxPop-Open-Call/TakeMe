import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FirebaseAuthenticationService } from '../../shared/services/firebase-authentication.service';
import { UserService } from '../../shared/services/user.service';
import { UserProvider } from '../../shared/providers/user-provider.service';
import { from } from 'rxjs';
import { LoadingControllerService } from '../../shared/services/loading-controller.service';
import { ToastService } from '../../shared/services/toast.service';
import { NgForm } from '@angular/forms';
import { ErrorWithKey } from '../../shared/models/error';
import { Logger } from '../../shared/providers/logger-provider.service';
import { DriverService } from '../../shared/services/driver.service';
import { CurrentItineraryProvider } from '../../shared/providers/current-itinerary-provider.service';
import { Role } from '../../shared/enums/role.enum';

@Component({
    selector: 'app-login',
    templateUrl: './login.page.html',
    styleUrls: ['./login.page.scss'],
})
export class LoginPage {

    showPwd = false;

    constructor(private router: Router,
                private firebaseAuthenticationService: FirebaseAuthenticationService,
                private userService: UserService,
                private userProvider: UserProvider,
                private currentItineraryProvider: CurrentItineraryProvider,
                private driverService: DriverService,
                private loadingControllerService: LoadingControllerService,
                private toastService: ToastService,
                private logger: Logger) {
    }

    login(form: NgForm) {
        this.firebaseAuthenticationService.logout().subscribe((result) => {
            this.userProvider.clearUserInfo();
            // clear other providers here
            // stop listening here

            this.loadingControllerService.createLoading('Logging in...', 5000).then((loading) => {
                loading.present();
                from(this.firebaseAuthenticationService.login(form.value.email, form.value.password)).subscribe(
                    () => {
                        this.handleLogin(loading);
                    }, (error) => {
                        loading.dismiss();
                        throw new ErrorWithKey(error.message, 'Login', null, error.code);
                    }
                );
            });
        }, (error) => {
            throw error;
        });
    }

    handleLogin(loading) {
        if (!this.firebaseAuthenticationService.userHasEmailVerified()) {
            this.logout();
            loading.dismiss();
            this.toastService.presentInfoToast('Por favor verifique o seu email e tente novamente');
        } else {
            return this.userService.getUserInformation().subscribe(
                (userInfo) => {
                    if (userInfo.body.organization.organizationType !== 'BUS_COMPANY') {
                        this.logout();
                        loading.dismiss();
                        this.toastService.presentErrorToast('Utilizador não encontrado');
                    } else {
                        const adminRole = userInfo.body.authorities.filter(role => role === Role.ROLE_BUS_COMPANY);
                        const driverRole = userInfo.body.authorities.filter(role => role === Role.ROLE_BUS_COMPANY_DRIVER);
                        if (userInfo.body.driverId === null && adminRole) {
                            this.logout();
                            loading.dismiss();
                            this.toastService.presentErrorToast('Não tem perfil de monitor. Contacte o nosso suporte.');
                            this.logger.debug('User with bus company admin profile tried to login.');
                        } else if (userInfo.body.driverId !== null && driverRole) {
                            this.userProvider.setUserInfo(userInfo.body);
                            let currentItinerary;
                            this.driverService.getCurrentItinerary(this.userProvider.getDriverId()).subscribe((currentItineraryResponse) => {
                                if (currentItineraryResponse.status === 204) {
                                    this.logger.debug('Received a code 204 response from backend, which means that there is no current itinerary for the monitor.' +
                                        ' Navigating to Itinerary List page...');
                                    this.router.navigateByUrl('/app');
                                } else {
                                    this.logger.debug('Received a current itinerary from backend. Stashing it in a provider.' +
                                        ' Navigating to Current Itinerary page...');
                                    currentItinerary = currentItineraryResponse.body;
                                    this.currentItineraryProvider.setCurrentItinerary(currentItinerary);
                                    this.router.navigateByUrl('/app/current-itinerary');
                                }
                                loading.dismiss();
                            }, (error) => {
                                loading.dismiss();
                                throw error;
                            });
                        } else {
                            this.logout();
                            loading.dismiss();
                            this.toastService.presentErrorToast('Não tem perfil de monitor. Contacte o nosso suporte.');
                        }
                    }
                }, (error) => {
                    loading.dismiss();
                    throw error;
                }
            );
        }

    }

    goToPasswordReset() {
        this.router.navigateByUrl('/auth/password-reset');
    }

    logout() {
        this.firebaseAuthenticationService.logout().subscribe(() => {
        }, (error) => {
            throw error;
        });
    }

}
