import {Injectable} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {ErrorDetail, ErrorWithKey} from '../models/error';
import * as StackTraceParser from 'error-stack-parser';
import {LocationStrategy, PathLocationStrategy} from '@angular/common';
import {UserProvider} from './user-provider.service';
import {NGXLogger} from 'ngx-logger';

@Injectable({
    providedIn: 'root'
})
export class Logger {


    constructor(private locationStrategy: LocationStrategy,
                private userProvider: UserProvider,
                private logger: NGXLogger
    ) {}

    debug(message: string, ...additional: any[]): void {
        this.logger.debug(message, additional);
    }

    error(message: string, error?: Error | HttpErrorResponse | ErrorWithKey): void {
        this.logger.error(message, this.addContextInfo(error));
    }

    /**
     * Create an error context information
     */
    addContextInfo(error: Error | HttpErrorResponse | ErrorWithKey): ErrorDetail {

        const errorDetail = new ErrorDetail();
        errorDetail.appId = 'app-condutores';
        errorDetail.user =  (this.userProvider.isUserFilled()) ? this.userProvider.getId() : null;
        if (error) {

            errorDetail.name = error.name || null;
            errorDetail.message = error.message;
            errorDetail.url = this.getPath();

            if (error instanceof HttpErrorResponse) {
                errorDetail.status = error.status;
                errorDetail.stack = null;
            } else if (error instanceof ErrorWithKey) {
                errorDetail.status = 1001;
                errorDetail.stack = null;
            } else {
                errorDetail.status = 1000;
                try {
                    errorDetail.stack = StackTraceParser.parse(error);
                } catch (e) {
                    errorDetail.stack = null;
                }
            }

        }

        return errorDetail;
    }

    private getPath(): string {
        return this.locationStrategy instanceof PathLocationStrategy ? this.locationStrategy.path() : '';
    }

}
