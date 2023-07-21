import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SubscriptionsService } from 'app/shared/services/subscriptions.service';
import { TranslateService } from '@ngx-translate/core';
import { OrganizationType } from 'app/shared/model/organization.model';
import { Account, Principal } from 'app/core';
import { ErrorHandlerProviderService } from "../../../shared/providers/error-handler-provider.service";

@Component({
    selector: 'jhi-children-import',
    templateUrl: './children-import.component.html',
    styles: []
})
export class ChildrenImportComponent implements OnInit {
    account: Account;
    type: OrganizationType;
    file?: File;
    uploading = false;

    readonly excelType: string = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';

    constructor(
        private principal: Principal,
        private router: Router,
        private subscriptionsService: SubscriptionsService,
        private alertService: ErrorHandlerProviderService,
        private translateService: TranslateService
    ) {}

    ngOnInit(): void {
        this.principal.identity().then(account => {
            this.account = account;
            this.type = this.account.organization.organizationType;
        });
    }

    goBack(): void {
        this.router.navigate(['bus-company/children-subscription']);
    }

    download(): void {
        this.subscriptionsService.download().subscribe(data => {
            const blob = new Blob([data.body], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;' });
            const url = window.URL.createObjectURL(blob);
            const anchor: HTMLAnchorElement = document.createElement('a');
            anchor.download = 'import_template';
            anchor.href = url;
            anchor.click();
        });
    }

    uploadFile(event: any): void {
        this.file = event.target.files[0];
    }

    upload(): void {
        if (this.file && this.file.type === this.excelType) {
            this.uploading = true;
            this.subscriptionsService.upload(this.file).subscribe(
                response => {
                    const childrenImportDTO = response.body.childrenImportDTO;
                    if (childrenImportDTO) {
                        if (childrenImportDTO.success) {
                            this.translateService
                                .get('familityBackofficeApp.childSubscription.import.success', {
                                    childrenImportedNumber:
                                        childrenImportDTO.createdChildrenNumber + childrenImportDTO.updatedChildrenNumber
                                })
                                .subscribe(msg => this.alertService.showSuccessMessage(msg));
                        } else {
                            if (childrenImportDTO.columnLetter) {
                                this.translateService
                                    .get('familityBackofficeApp.childSubscription.import.error.parsing', {
                                        lineNumber: childrenImportDTO.lineNumber,
                                        columnLetter: childrenImportDTO.columnLetter
                                    })
                                    .subscribe(msg => this.alertService.showErrorMessage(msg));
                            } else if (childrenImportDTO.lineNumber) {
                                this.translateService
                                    .get('familityBackofficeApp.childSubscription.import.error.processing', {
                                        lineNumber: childrenImportDTO.lineNumber
                                    })
                                    .subscribe(msg => this.alertService.showErrorMessage(msg));
                            } else {
                                this.translateService
                                    .get('familityBackofficeApp.childSubscription.import.error.max-children')
                                    .subscribe(msg => this.alertService.showErrorMessage(msg));
                            }
                        }
                    }
                    this.uploading = false;
                    this.goBack();
                },
                error => {
                    this.translateService
                        .get('familityBackofficeApp.childSubscription.import.error.other', {})
                        .subscribe(msg => this.alertService.showErrorMessage(msg));
                    this.uploading = false;
                    this.goBack();
                }
            );
        } else {
            this.translateService
                .get('familityBackofficeApp.childSubscription.import.error.no-file')
                .subscribe(msg => this.alertService.showErrorMessage(msg));
        }
    }
}
