import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {IonicModule} from '@ionic/angular';
import {FormsModule} from '@angular/forms';

import {PasswordResetPage} from './password-reset.page';

import {PasswordResetRoutingModule} from './password-reset-routing.module';

@NgModule({
    imports: [
        CommonModule,
        IonicModule,
        FormsModule,
        PasswordResetRoutingModule
    ],
    declarations: [
        PasswordResetPage
    ]
})
export class PasswordResetPageModule {
}
