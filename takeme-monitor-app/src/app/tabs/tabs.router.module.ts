import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TabsPage } from './tabs.page';

const routes: Routes = [
    {
        path: '',
        component: TabsPage,
        children: [
            {
                path: '',
                redirectTo: '/app/itineraries',
                pathMatch: 'full'
            },
            {
                path: 'itineraries',
                children: [
                    {
                        path: '',
                        loadChildren: '../itineraries/itineraries.module#ItinerariesPageModule'
                    }
                ]
            },
            {
                path: 'current-itinerary',
                loadChildren: '../current-itinerary/current-itinerary.module#CurrentItineraryPageModule'
            },
            {
                path: 'settings',
                children: [
                    {
                        path: '',
                        loadChildren: '../settings/settings.module#SettingsPageModule'
                    }
                ]
            }
        ]
    }
];

@NgModule({
    imports: [
        RouterModule.forChild(routes)
    ],
    exports: [RouterModule]
})
export class TabsPageRoutingModule {
}
