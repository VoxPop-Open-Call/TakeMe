import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Routes, RouterModule} from '@angular/router';
import {SettingsRoutingModule} from './settings-routing.module';

import {IonicModule} from '@ionic/angular';

import {SettingsPage} from './settings.page';


@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        IonicModule,
        SettingsRoutingModule
    ],
    declarations: [SettingsPage]
})
export class SettingsPageModule {
}
