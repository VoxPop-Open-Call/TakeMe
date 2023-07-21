import { NgModule } from '@angular/core';
import { FamilityBackofficeSharedModule } from 'app/shared/shared.module';
import { PromoterStopPointComponent } from './list/promoter-stop-point.component';
import { PromoterStopPointDetailComponent } from './detail/promoter-stop-point-detail.component';
import { PromoterStopPointUpdateComponent } from './update/promoter-stop-point-update.component';
import { PromoterStopPointDeleteDialogComponent } from './delete/promoter-stop-point-delete-dialog.component';
import { PromoterStopPointRoutingModule } from './route/promoter-stop-point-routing.module';
import {ReactiveFormsModule} from "@angular/forms";

@NgModule({
  imports: [FamilityBackofficeSharedModule, PromoterStopPointRoutingModule, ReactiveFormsModule],
  declarations: [
    PromoterStopPointComponent,
    PromoterStopPointDetailComponent,
    PromoterStopPointUpdateComponent,
    PromoterStopPointDeleteDialogComponent,
  ],
})
export class PromoterStopPointModule {}
