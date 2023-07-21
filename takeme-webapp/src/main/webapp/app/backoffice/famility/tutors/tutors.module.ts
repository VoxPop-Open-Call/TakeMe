import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgxPaginationModule } from 'ngx-pagination';

import { TutorsComponent } from './tutors.component';
import { TutorDetailComponent } from './tutor-detail.component';
import { TutorChildrenComponent } from './tutor-children.component';
import { TutorChangeStatusDialogComponent } from './tutor-change-status-dialog.component';

import { TutorsService } from './tutors.service';

import { tutorsRoute } from './tutors.route';
import { FamilityBackofficeSharedModule } from 'app/shared';
import { TableModule } from "primeng/table";

const ROUTES = [...tutorsRoute];

@NgModule({
    imports: [CommonModule, NgxPaginationModule, FormsModule, RouterModule.forChild(ROUTES), FamilityBackofficeSharedModule, TableModule],
    declarations: [TutorsComponent, TutorDetailComponent, TutorChildrenComponent, TutorChangeStatusDialogComponent],
    entryComponents: [TutorChangeStatusDialogComponent],
    providers: [TutorsService],
    exports: [TutorsComponent, TutorChildrenComponent]
})
export class TutorsModule {}
