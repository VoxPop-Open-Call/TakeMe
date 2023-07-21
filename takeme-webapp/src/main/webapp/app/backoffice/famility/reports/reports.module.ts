import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportsComponent } from './reports.component';
import { FamilityBackofficeSharedModule } from 'app/shared';
import { RouterModule } from '@angular/router';
import { reportsRoute } from './reports.route';

const ROUTES = [...reportsRoute];

@NgModule({
    imports: [CommonModule, RouterModule.forChild(ROUTES), FamilityBackofficeSharedModule],
    declarations: [ReportsComponent]
})
export class ReportsModule {}
