import {Component} from '@angular/core';
import {Router} from '@angular/router';

@Component({
    selector: 'app-tabs',
    templateUrl: 'tabs.page.html',
    styleUrls: ['tabs.page.scss']
})
export class TabsPage {

    tabBarSelected: string;

    constructor(private router: Router) {

    }

    selected(seleted) {
        if (seleted) {
            this.tabBarSelected = seleted.detail.tab;
        }
    }

    changeTab(tab: string) {
        this.router.navigateByUrl('/app/' + tab);
    }
}
