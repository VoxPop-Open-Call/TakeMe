import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxPaginationModule } from 'ngx-pagination';
import { ItinerariesComponent } from './list/itineraries.component';
import { itinerariesRoute } from './route/itineraries.route';
import { FamilityBackofficeSharedModule } from 'app/shared';
import { Principal } from "app/core";
import { TableModule } from "primeng/table";
import { ButtonModule } from "primeng/button";

const ROUTES = [...itinerariesRoute];

@NgModule({
    imports: [CommonModule, FormsModule, NgxPaginationModule, RouterModule.forChild(ROUTES), FamilityBackofficeSharedModule, TableModule, ButtonModule, ReactiveFormsModule],
    declarations: [
        ItinerariesComponent
    ],
    providers: [Principal],
    exports: [ItinerariesComponent]
})
export class ItinerariesModule {
}
