import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppVersion } from '@ionic-native/app-version/ngx';
import { Insomnia } from '@ionic-native/insomnia/ngx';
import { LocationLinkModule } from './components/location-link/location-link.module';

@NgModule({
    declarations: [],
    imports: [
        CommonModule
        
    ],
    providers: [
        AppVersion,
        Insomnia
    ],
    exports: [LocationLinkModule]
})
export class SharedModule { }
