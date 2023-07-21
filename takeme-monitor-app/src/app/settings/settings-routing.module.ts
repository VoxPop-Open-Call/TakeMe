import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { SettingsPage } from './settings.page';

const routes: Routes = [

  {
      path: 'about',
      loadChildren: './about/about.module#AboutPageModule'
  },
  {
      path: 'change-password',
      loadChildren: './change-password/change-password.module#ChangePasswordPageModule'
  },
  {
      path: '',
      component: SettingsPage,
      pathMatch: 'full'
  },
  { path: 'contacts', loadChildren: './about/contacts/contacts.module#ContactsPageModule' },
  { path: 'faq', loadChildren: './about/faq/faq.module#FaqPageModule' },
  { path: 'terms-and-conditions', loadChildren: './about/terms-and-conditions/terms-and-conditions.module#TermsAndConditionsPageModule' },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SettingsRoutingModule { }
