import { NgModule } from '@angular/core';

import { FamilityBackofficeSharedLibsModule, FindLanguageFromKeyPipe, AlertComponent, AlertErrorComponent } from './';

@NgModule({
    imports: [FamilityBackofficeSharedLibsModule],
    declarations: [FindLanguageFromKeyPipe, AlertComponent, AlertErrorComponent],
    exports: [FamilityBackofficeSharedLibsModule, FindLanguageFromKeyPipe, AlertComponent, AlertErrorComponent]
})
export class FamilityBackofficeSharedCommonModule {}
