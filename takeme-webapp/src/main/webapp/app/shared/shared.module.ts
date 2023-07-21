import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import {
    FamilityBackofficeSharedLibsModule,
    FamilityBackofficeSharedCommonModule,
    JhiLoginModalComponent,
    HasAnyAuthorityDirective
} from './';

import { TotalElementsHeader } from './util/total-elements-header';
import { BuildAddress } from './util/build-address';
import { DaysOfWeekComponent } from 'app/shared/components/days-of-week/days-of-week.component';
import { ComposedLocationComponent } from 'app/shared/components/composed-location/composed-location.component';
import { CoordinatesLinkComponent } from 'app/shared/components/coordinates-link/coordinates-link.component';
import { CollectionComponent } from 'app/shared/components/collection/collection.component';
import { DeliveryComponent } from 'app/shared/components/delivery/delivery.component';
import { TranslateDirective } from "./language/translate.directive";
import { FormatExpressionDateTimePipe } from "./util/format-expression-datetime.pipe";
import { ItemCountComponent } from "./pagination/item-count.component";
import { FormatExpressionDatePipe } from "./util/format-expression-date.pipe";
import { FilterComponent } from "./filter/filter.component";
import { SortDirective } from './sort/sort.directive';
import { FormatMediumDatetimePipe } from './date/format-medium-datetime.pipe';
import { FormatMediumDatePipe } from './date/format-medium-date.pipe';
import { FormatMediumTimePipe } from "./date/format-medium-time.pipe";
import {NgbDateCustomAdapter} from "../config/datepicker-adapter";
import {FormatExpressionTimePipe} from "./util/format-expression-time.pipe";

@NgModule({
    imports: [FamilityBackofficeSharedLibsModule, FamilityBackofficeSharedCommonModule],
    declarations: [
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        DaysOfWeekComponent,
        ComposedLocationComponent,
        CoordinatesLinkComponent,
        CollectionComponent,
        DeliveryComponent,
        TranslateDirective,
        FormatExpressionDatePipe,
        FormatExpressionDateTimePipe,
        FormatExpressionTimePipe,
        ItemCountComponent,
        FilterComponent,
        FormatMediumDatetimePipe,
        FormatMediumDatePipe,
        FormatMediumTimePipe,
        SortDirective
    ],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateCustomAdapter }, TotalElementsHeader, BuildAddress],
    entryComponents: [JhiLoginModalComponent],
    exports: [
        FamilityBackofficeSharedCommonModule,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        DaysOfWeekComponent,
        ComposedLocationComponent,
        CoordinatesLinkComponent,
        CollectionComponent,
        DeliveryComponent,
        TranslateDirective,
        FormatExpressionDatePipe,
        FormatExpressionDateTimePipe,
        FormatExpressionTimePipe,
        ItemCountComponent,
        FilterComponent,
        FormatMediumDatetimePipe,
        FormatMediumDatePipe,
        FormatMediumTimePipe,
        SortDirective
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FamilityBackofficeSharedModule {
    static forRoot() {
        return {
            ngModule: FamilityBackofficeSharedModule
        };
    }
}
