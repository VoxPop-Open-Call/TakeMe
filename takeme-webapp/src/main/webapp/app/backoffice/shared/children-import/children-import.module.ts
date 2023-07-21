import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChildrenImportComponent } from 'app/backoffice/shared/children-import/children-import.component';
import { FamilityBackofficeSharedModule } from 'app/shared';

@NgModule({
    imports: [CommonModule, FamilityBackofficeSharedModule],
    declarations: [ChildrenImportComponent]
})
export class ChildrenImportModule {}
