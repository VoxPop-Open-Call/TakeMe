import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
    imports: [InfiniteScrollModule, FontAwesomeModule],
    exports: [FormsModule, CommonModule, NgbModule, InfiniteScrollModule, FontAwesomeModule, TranslateModule]
})
export class FamilityBackofficeSharedLibsModule {
    static forRoot() {
        return {
            ngModule: FamilityBackofficeSharedLibsModule
        };
    }
}
