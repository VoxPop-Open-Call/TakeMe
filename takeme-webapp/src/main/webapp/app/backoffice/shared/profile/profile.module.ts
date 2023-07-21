import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';

import { ProfileComponent } from './profile.component';
import { ProfileDialogNewPhotoComponent } from './profile-dialog-new-photo.component';

import { profileRoute } from './profile.route';
import { FamilityBackofficeSharedModule } from 'app/shared';

const ROUTES = [...profileRoute];

@NgModule({
    imports: [CommonModule, ReactiveFormsModule, RouterModule.forChild(ROUTES), FamilityBackofficeSharedModule],
    declarations: [ProfileComponent, ProfileDialogNewPhotoComponent],
    entryComponents: [ProfileDialogNewPhotoComponent]
})
export class ProfileModule {}
