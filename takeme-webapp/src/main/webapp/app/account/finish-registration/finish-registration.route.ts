import {Route} from '@angular/router';

import {FinishRegistrationComponent} from './finish-registration.component';

export const finishRegistrationRoute: Route = {
  path: 'finish-registration',
  component: FinishRegistrationComponent,
  data: {
    authorities: [],
    pageTitle: 'finishregistration.title'
  }
};
