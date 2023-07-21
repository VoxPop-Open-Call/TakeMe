import { Route } from '@angular/router';

import { PasswordResetInitComponent } from './password-reset-init.component';

export const passwordResetInitRoute: Route = {
    path: 'reset-password',
    component: PasswordResetInitComponent,
    data: {
        authorities: [],
        pageTitle: 'global.menu.account.password'
    }
};
