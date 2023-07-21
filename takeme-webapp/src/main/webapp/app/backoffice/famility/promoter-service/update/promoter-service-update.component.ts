import {Component, OnInit, ElementRef, OnDestroy} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';
import {finalize} from 'rxjs/operators';
import {PromoterServiceFormService, PromoterServiceFormGroup} from './promoter-service-form.service';
import {IPromoterService, TransportType} from '../promoter-service.model';
import {PromoterServiceService} from '../service/promoter-service.service';
import {AlertError} from 'app/shared/alert/alert-error.model';
import {EventManager, EventWithContent} from 'app/core/util/event-manager.service';
import {DataUtils, FileLoadError} from 'app/core/util/data-util.service';
import {ErrorHandlerProviderService} from "../../../../shared/providers/error-handler-provider.service";

@Component({
    selector: 'jhi-famility-promoter-service-update',
    templateUrl: './promoter-service-update.component.html',
})
export class PromoterServiceUpdateComponent implements OnInit, OnDestroy {
    service: IPromoterService | null = null;

    editForm: PromoterServiceFormGroup = this.promoterServiceFormService.createPromoterServiceFormGroup();
    isSaving = false;

    transportTypeOptions: string[] = [
        TransportType.CAR,
        TransportType.BUS,
        TransportType.WALKING,
        TransportType.BICYCLING
    ];

    constructor(
        private promoterServiceService: PromoterServiceService,
        private promoterServiceFormService: PromoterServiceFormService,
        private errorHandler: ErrorHandlerProviderService,
        private activatedRoute: ActivatedRoute,
        private elementRef: ElementRef,
        private dataUtils: DataUtils,
        private eventManager: EventManager
    ) {
    }

    ngOnInit(): void {
        this.activatedRoute.data.subscribe(({promoterService}) => {
            this.service = promoterService;

            if (promoterService) {
                this.promoterServiceFormService.resetForm(this.editForm, promoterService);
            }
        });
    }

    ngOnDestroy(): void {
        this.errorHandler.clean();
    }

    save(): void {
        this.isSaving = true;

        const promoterService = this.promoterServiceFormService.getPromoterService(this.editForm);
        if (promoterService.id !== null) {
            this.promoterServiceService
                .updatePromoterServiceByPromoterServiceId(promoterService.id, promoterService)
                .pipe(finalize(() => this.isSaving = false))
                .subscribe({
                    next: () => this.previousState(),
                    error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                });
        } else {
            this.promoterServiceService
                .createPromoterService(promoterService)
                .pipe(finalize(() => this.isSaving = false))
                .subscribe({
                    next: () => this.previousState(),
                    error: (error: HttpErrorResponse) => this.errorHandler.showError(error)
                });
        }
    }

    previousState(): void {
        window.history.back();
    }

    byteSize(base64String: string): string {
        return this.dataUtils.byteSize(base64String);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string): void {
        this.editForm.patchValue({
            [field]: null,
            [fieldContentType]: null,
        });

        if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
            this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
        }
    }

    setFileData(event: Event, field: string, isImage: boolean): void {
        this.dataUtils
            .loadFileToForm(event, this.editForm, field, isImage)
            .subscribe({
                error: (err: FileLoadError) => this.eventManager.broadcast(new EventWithContent<AlertError>('familityBackofficeApp.error', {
                    ...err,
                    key: 'error.file.' + err.key
                })),
            });
    }
}
