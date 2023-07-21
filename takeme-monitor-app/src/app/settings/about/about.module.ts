import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { AboutPage } from './about.page';

const routes: Routes = [
  {path: 'faq', loadChildren: './faq/faq.module#FaqPageModule'},
  {path: 'contacts', loadChildren: './contacts/contacts.module#ContactsPageModule'},
  {path: 'terms-and-conditions', loadChildren: './terms-and-conditions/terms-and-conditions.module#TermsAndConditionsPageModule'},
  {
      path: '',
      component: AboutPage,
      pathMatch: 'full'
  },
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild(routes)
  ],
  declarations: [AboutPage]
})
export class AboutPageModule {}
