import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule } from '@angular/forms';

import { UsersComponent } from './users.component';
import { UsersDialogChangeStatusComponent } from './users-dialog-change-status.component';

import { usersRoute } from './users.route';
import { FamilityBackofficeSharedModule } from 'app/shared';
import { TableModule } from "primeng/table";

const ROUTES = [...usersRoute];

@NgModule({
  imports: [CommonModule, FormsModule, NgxPaginationModule, RouterModule.forChild(ROUTES), FamilityBackofficeSharedModule, TableModule],
    declarations: [UsersComponent, UsersDialogChangeStatusComponent],
    entryComponents: [UsersDialogChangeStatusComponent]
})
export class UsersModule {}
