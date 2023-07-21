import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Itinerary } from 'src/app/shared/models/itinerary';
import { ItineraryProvider } from '../providers/itinerary.provider.service';

@Component({
    selector: 'app-itinerary',
    templateUrl: './itinerary.component.html',
    styleUrls: ['./itinerary.component.scss']
})
export class ItineraryComponent {

    @Input()
    itinerary: Itinerary;

    constructor(private router: Router,
        private itineraryProvider: ItineraryProvider) { }

    goToItineraryDetail() {
        this.itineraryProvider.setItinerary(this.itinerary);
        this.router.navigateByUrl('/app/itineraries/itinerary-details');
    }

}
