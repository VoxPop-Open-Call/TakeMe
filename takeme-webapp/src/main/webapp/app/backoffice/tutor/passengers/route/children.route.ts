import {Routes} from '@angular/router';
import {UserRouteAccessService} from 'app/core';
import {ChildrenComponent} from '../list/children.component';
import {ChildUpdateComponent} from "../update/child-update.component";

export const childrenRoute: Routes = [
    {
        path: 'tutor/passengers',
        component: ChildrenComponent,
        data: {
            authorities: ['ROLE_TUTOR'],
            pageTitle: 'backoffice.home.tutors.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tutor/passengers/new',
        component: ChildUpdateComponent,
        data: {
            authorities: ['ROLE_TUTOR'],
            pageTitle: 'backoffice.home.tutors.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tutor/passengers/:id/edit',
        component: ChildUpdateComponent,
        data: {
            authorities: ['ROLE_TUTOR'],
            pageTitle: 'backoffice.home.tutors.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
