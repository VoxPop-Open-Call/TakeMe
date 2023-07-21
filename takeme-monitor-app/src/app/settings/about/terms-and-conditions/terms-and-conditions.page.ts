import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-terms-and-conditions',
  templateUrl: './terms-and-conditions.page.html',
  styleUrls: ['./terms-and-conditions.page.scss'],
})
export class TermsAndConditionsPage implements OnInit {

  fromInformation: string;

    constructor(private router: Router,
        private route: ActivatedRoute) {}

    ngOnInit() {
        this.route.params.subscribe(param => {
            this.fromInformation = param.from;
        });
    }

}
