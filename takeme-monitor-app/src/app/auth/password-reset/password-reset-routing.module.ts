import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {PasswordResetPage} from './password-reset.page';

const routes: Routes = [
    {path: '', component: PasswordResetPage},
    {path: 'check-email', loadChildren: './check-email/check-email.module#CheckEmailPageModule' }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class PasswordResetRoutingModule {

}
