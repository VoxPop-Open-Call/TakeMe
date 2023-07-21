import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { AlertService } from "../../core/util/alert.service";

@Injectable({
    providedIn: 'root'
})
export class ErrorHandlerProviderService {
    constructor(private alertService: AlertService) {}

    showError(error: HttpErrorResponse) {
      this.alertService.addAlert({ type: 'danger', message: error.error.message });
    }

    showErrorMessage(message: string) {
      this.alertService.addAlert({ type: 'danger', message });
    }

    showSuccessMessage(message: string) {
      this.alertService.addAlert({ type: 'success', message });
    }

    clean() {
        this.alertService.clear();
    }
}
