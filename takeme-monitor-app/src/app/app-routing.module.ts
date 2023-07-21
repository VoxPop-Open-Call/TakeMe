import {NgModule} from '@angular/core';
import {PreloadAllModules, RouterModule, Routes} from '@angular/router';

const routes: Routes = [
    {path: '', loadChildren: './auth/auth.module#AuthModule'},
    {path: 'auth', loadChildren: './auth/auth.module#AuthModule'},
    {path: 'app', loadChildren: './tabs/tabs.module#TabsPageModule'},
];

@NgModule({
    imports: [
        RouterModule.forRoot(routes, {preloadingStrategy: PreloadAllModules})
    ],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
