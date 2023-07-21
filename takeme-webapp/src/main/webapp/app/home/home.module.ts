import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {FamilityBackofficeSharedModule} from 'app/shared';
import {RouterModule} from '@angular/router';
import {HOME_ROUTE, HomeComponent} from './';

@NgModule({
    imports: [FamilityBackofficeSharedModule, RouterModule.forChild([HOME_ROUTE])],
    declarations: [HomeComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FamilityBackofficeHomeModule {
}
