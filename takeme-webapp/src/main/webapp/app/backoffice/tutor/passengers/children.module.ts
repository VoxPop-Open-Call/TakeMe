import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgxPaginationModule} from 'ngx-pagination';
import {ChildrenComponent} from './list/children.component';
import {ChildUpdateComponent} from "./update/child-update.component";
import {childrenRoute} from './route/children.route';
import {FamilityBackofficeSharedModule} from 'app/shared';
import {ChildrenService} from "../../../shared/services/children.service";
import {Principal} from "app/core";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";

const ROUTES = [...childrenRoute];

@NgModule({
    imports: [CommonModule, FormsModule, NgxPaginationModule, RouterModule.forChild(ROUTES), FamilityBackofficeSharedModule, TableModule, ButtonModule, ReactiveFormsModule],
    declarations: [
        ChildrenComponent,
        ChildUpdateComponent
    ],
    providers: [ChildrenService, Principal],
    exports: [ChildrenComponent]
})
export class ChildrenModule {
}
