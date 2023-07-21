import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { DEBUG_INFO_ENABLED } from './app/app.constants';
import { FamilityBackofficeAppModule } from './app/app.module';
import { environment } from "./environments/environment";

// disable debug data on prod profile to improve performance
if (!DEBUG_INFO_ENABLED) {
  enableProdMode();
}

(async () => {
  environment.config = await fetch('api/angular-env-configuration').then((res) => res.json());
  platformBrowserDynamic()
    .bootstrapModule(FamilityBackofficeAppModule, { preserveWhitespaces: true })
    // eslint-disable-next-line no-console
    .then(() => console.log('Application started'))
    .catch(err => console.error(err));
})();
