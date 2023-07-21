import { LOCALE_ID, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbDateAdapter, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { NgbDateCustomAdapter } from './config/datepicker-adapter';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { FamilityBackofficeSharedModule } from 'app/shared';
import { FamilityBackofficeCoreModule } from 'app/core';
import { FamilityBackofficeAppRoutingModule } from './app-routing.module';
import { FamilityBackofficeHomeModule } from './home';
import { FamilityBackofficeAccountModule } from './account/account.module';
import { FamilityBackofficeEntityModule } from './entities/entity.module';
import moment from 'moment';
import './config/dayjs';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import {
  ActiveMenuDirective,
  ErrorComponent,
  FooterComponent,
  JhiMainComponent,
  NavbarComponent,
  PageRibbonComponent
} from './layouts';

import { BackOfficeModule } from 'app/backoffice/backoffice.module';
import { Environment } from '../environments/environment';
import { TranslationModule } from "./shared/language/translation.module";
import { ApplicationConfigService } from "./core/config/application-config.service";
import { FaIconLibrary } from "@fortawesome/angular-fontawesome";
import { registerLocaleData } from "@angular/common";
import locale from "@angular/common/locales/en";
import { fontAwesomeIcons } from "./config/font-awesome-icons";

@NgModule({
  imports: [
    BrowserModule,
    FamilityBackofficeAppRoutingModule,
    NgxWebstorageModule.forRoot({prefix: 'jhi', separator: '-'}),
    FamilityBackofficeSharedModule.forRoot(),
    FamilityBackofficeCoreModule,
    FamilityBackofficeHomeModule,
    FamilityBackofficeAccountModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    FamilityBackofficeEntityModule,
    BackOfficeModule,
    TranslationModule
  ],
  declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  providers: [
    { provide: LOCALE_ID, useValue: 'en' },
    Environment,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthExpiredInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorHandlerInterceptor,
      multi: true
    },
    { provide: NgbDateAdapter, useClass: NgbDateCustomAdapter },
  ],
  bootstrap: [JhiMainComponent]
})
export class FamilityBackofficeAppModule {
  constructor(applicationConfigService: ApplicationConfigService, iconLibrary: FaIconLibrary, dpConfig: NgbDatepickerConfig, appConfig: Environment) {
    applicationConfigService.setEndpointPrefix(SERVER_API_URL);
    registerLocaleData(locale);
    iconLibrary.addIcons(...fontAwesomeIcons);
    dpConfig.minDate = {year: moment().year() - 100, month: 1, day: 1};
  }
}
