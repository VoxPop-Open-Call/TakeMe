import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FamilityBackofficeSharedModule } from 'app/shared';
import { organizationRoute } from "./organization.route";
import { OrganizationDetailComponent } from "./organization-detail.component";

const ENTITY_STATES = [...organizationRoute];

@NgModule({
    imports: [FamilityBackofficeSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OrganizationDetailComponent,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FamilityBackofficeOrganizationModule {}
