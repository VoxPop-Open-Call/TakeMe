import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';

const routes: Routes = [
    {path: 'login', loadChildren: './login/login.module#LoginPageModule'},
    {path: 'password-reset', loadChildren: './password-reset/password-reset.module#PasswordResetPageModule'},
    {path: '', redirectTo: 'login'},
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AuthRoutingModule {

}
