import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {FamilityBackofficeSharedModule} from 'app/shared';

import {
  PasswordStrengthBarComponent,
  RegisterComponent,
  ActivateComponent,
  PasswordComponent,
  PasswordResetInitComponent,
  PasswordResetFinishComponent,
  SettingsComponent,
  FinishRegistrationComponent,
  accountState
} from './';
import {ReactiveFormsModule} from "@angular/forms";

@NgModule({
  imports: [FamilityBackofficeSharedModule, RouterModule.forChild(accountState), ReactiveFormsModule],
  declarations: [
    ActivateComponent,
    RegisterComponent,
    PasswordComponent,
    PasswordStrengthBarComponent,
    PasswordResetInitComponent,
    PasswordResetFinishComponent,
    SettingsComponent,
    FinishRegistrationComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FamilityBackofficeAccountModule {
}
