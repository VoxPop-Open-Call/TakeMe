import {NgModule} from '@angular/core';
import {FamilityBackofficeSharedModule} from 'app/shared/shared.module';
import {PromoterServiceComponent} from './list/promoter-service.component';
import {PromoterServiceUpdateComponent} from './update/promoter-service-update.component';
import {PromoterServiceRoutingModule} from './route/promoter-service-routing.module';
import {ReactiveFormsModule} from "@angular/forms";
import {ButtonModule} from "primeng/button";
import {SharedModule} from "primeng/api";
import {TableModule} from "primeng/table";

@NgModule({
    imports: [FamilityBackofficeSharedModule, PromoterServiceRoutingModule, ReactiveFormsModule, ButtonModule, SharedModule, TableModule],
    declarations: [
        PromoterServiceComponent,
        PromoterServiceUpdateComponent,
    ],
})
export class PromoterServiceModule {
}
