import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core';

import { TutorsComponent } from './tutors.component';
import { TutorDetailComponent } from './tutor-detail.component';
import { TutorChildrenComponent } from './tutor-children.component';

export const tutorsRoute: Routes = [
    {
        path: 'promoter/tutors',
        component: TutorsComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'familityBackofficeApp.tutor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promoter/tutors/:id',
        component: TutorDetailComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'familityBackofficeApp.tutor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'promoter/tutors/:id/children',
        component: TutorChildrenComponent,
        data: {
            authorities: ['ROLE_FAMILITY'],
            pageTitle: 'familityBackofficeApp.tutor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
