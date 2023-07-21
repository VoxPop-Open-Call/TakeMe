import {ErrorHandler, Injectable, Injector} from '@angular/core';
import {BAD_REQUEST, FORBIDDEN, UNAUTHORIZED} from 'http-status-codes';
import {HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {ErrorWithKey} from '../models/error';
import {ToastService} from '../services/toast.service';
import {UserProvider} from './user-provider.service';
import {Logger} from './logger-provider.service';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {

    static readonly ERROR_MESSAGE_PREFIX: string = 'error.';
    static readonly GENERIC_ERROR_MESSAGE: string = 'generic-error';
    static readonly NO_INTERNET_AVAILABLE: string = 'without-internet';

    constructor(private injector: Injector,
                private toastService: ToastService,
                private translateService: TranslateService,
                private userProvider: UserProvider,
                private logger: Logger
    ) {}

    handleError(error: Error | HttpErrorResponse | ErrorWithKey): void {

        this.logger.error('Handling error on global error handler ', error);

        if (error instanceof HttpErrorResponse) {

            // Server or connection error happened
            if (!navigator.onLine) {
                this.showErrorWithKey(GlobalErrorHandler.NO_INTERNET_AVAILABLE);
            } else {
                const router = this.injector.get(Router);
                // Handle Http Error (error.status === 403, 404...)
                const httpErrorCode = error.status;
                switch (httpErrorCode) {
                    case UNAUTHORIZED:
                        router.navigateByUrl('/login');
                        break;
                    case FORBIDDEN:
                        router.navigateByUrl('/login');
                        break;
                    case BAD_REQUEST:
                        this.showError(error);
                        break;
                    default:
                        this.showErrorWithKey(GlobalErrorHandler.GENERIC_ERROR_MESSAGE, error.message);
                }
            }
        } else if (error instanceof ErrorWithKey) {
            this.showErrorWithKey(error.key);
        } else {
            // Handle Client Error (Angular Error, ReferenceError...)
            // this.toastService.presentErrorToast(error.message);
            this.showErrorWithKey(GlobalErrorHandler.GENERIC_ERROR_MESSAGE, error.message);
        }
    }

    async showErrorWithKey(errorKey: string, errorMessage?: string) {

        const errorMessageKey = GlobalErrorHandler.ERROR_MESSAGE_PREFIX + errorKey;
        let message = await this.translateService.get(errorMessageKey).toPromise();
        if (message === errorMessageKey) {
            message = await this.translateService
                .get(GlobalErrorHandler.ERROR_MESSAGE_PREFIX + GlobalErrorHandler.GENERIC_ERROR_MESSAGE).toPromise();
        }
        if (errorMessage) {
            message = message + ' (' + errorMessage + ')';
        }
        this.toastService.presentErrorToast(message);
    }

    showError(error: HttpErrorResponse) {

        if (error.error && error.error.errorKey) {
            this.showErrorWithKey(error.error.errorKey);
        } else {
            // this.toastService.presentErrorToast(error.message);
            this.showErrorWithKey(GlobalErrorHandler.GENERIC_ERROR_MESSAGE, error.message);
        }
    }

}
