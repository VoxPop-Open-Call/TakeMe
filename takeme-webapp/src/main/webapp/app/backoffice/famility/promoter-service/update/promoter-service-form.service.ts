import {Injectable} from '@angular/core';
import {FormGroup, FormControl, Validators, AbstractControl} from '@angular/forms';
import {IPromoterService, NewPromoterService} from '../promoter-service.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPromoterService for edit and NewPromoterServiceFormGroupInput for create.
 */
type PromoterServiceFormGroupInput = IPromoterService | PartialWithRequiredKeyOf<NewPromoterService>;

type PromoterServiceFormDefaults = Pick<NewPromoterService, 'id' | 'needsETA'>;

type PromoterServiceFormGroupContent = {
    id: FormControl<IPromoterService['id'] | NewPromoterService['id']>;
    name: FormControl<IPromoterService['name']>;
    logo: FormControl<IPromoterService['logo']>;
    logoContentType: FormControl<IPromoterService['logoContentType']>;
    startDate: FormControl<IPromoterService['startDate']>;
    endDate: FormControl<IPromoterService['endDate']>;
    needsETA: FormControl<IPromoterService['needsETA']>;
    enrollmentURL: FormControl<IPromoterService['enrollmentURL']>;
    transportType: FormControl<IPromoterService['transportType']>;
};

export type PromoterServiceFormGroup = FormGroup<PromoterServiceFormGroupContent>;

@Injectable({providedIn: 'root'})
export class PromoterServiceFormService {
    createPromoterServiceFormGroup(promoterService: PromoterServiceFormGroupInput = {id: null}): PromoterServiceFormGroup {
        const promoterServiceRawValue = {
            ...this.getFormDefaults(),
            ...promoterService,
        };
        return new FormGroup<PromoterServiceFormGroupContent>({
            id: new FormControl(
                {value: promoterServiceRawValue.id, disabled: true},
                {
                    nonNullable: true,
                    validators: [Validators.required],
                }
            ),
            name: new FormControl(promoterServiceRawValue.name, {
                validators: [Validators.required],
            }),
            logo: new FormControl(promoterServiceRawValue.logo),
            logoContentType: new FormControl(promoterServiceRawValue.logoContentType),
            startDate: new FormControl(promoterServiceRawValue.startDate, {
                validators: [Validators.required, this.startDateValidator],
            }),
            endDate: new FormControl(promoterServiceRawValue.endDate, {
                validators: [this.endDateValidator],
            }),
            needsETA: new FormControl(promoterServiceRawValue.needsETA),
            enrollmentURL: new FormControl(promoterServiceRawValue.enrollmentURL, {
                validators: [Validators.required],
            }),
            transportType: new FormControl(promoterServiceRawValue.transportType, {
                validators: [Validators.required],
            }),
        });
    }

    getPromoterService(form: PromoterServiceFormGroup): IPromoterService | NewPromoterService {
        return form.getRawValue() as IPromoterService | NewPromoterService;
    }

    resetForm(form: PromoterServiceFormGroup, promoterService: PromoterServiceFormGroupInput): void {
        const promoterServiceRawValue = {...this.getFormDefaults(), ...promoterService};
        form.reset(
            {
                ...promoterServiceRawValue,
                id: {value: promoterServiceRawValue.id, disabled: true},
            } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
        );
    }

    private getFormDefaults(): PromoterServiceFormDefaults {
        return {
            id: null,
            needsETA: false,
        };
    }

    private startDateValidator(control: AbstractControl): { [key: string]: boolean } | null {
        const startDate = control.value;
        const endDate = control.parent?.get('endDate')?.value;

        if (startDate && endDate && startDate > endDate) {
            return {'startDateAfterEndDate': true};
        }

        return null;
    }

    private endDateValidator(control: AbstractControl): { [key: string]: boolean } | null {
        const startDate = control.parent?.get('startDate')?.value;
        const endDate = control.value;

        if (startDate && endDate && startDate > endDate) {
            return {'endDateBeforeStartDate': true};
        }

        return null;
    }
}
