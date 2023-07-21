import { Component } from '@angular/core';
import { UserProvider } from '../shared/providers/user-provider.service';
import { Itinerary } from '../shared/models/itinerary';
import { DriverService } from '../shared/services/driver.service';
import { ItineraryProvider } from './providers/itinerary.provider.service';
import { Router } from '@angular/router';
import { ItineraryStatusType } from '../shared/enums/itinerary-status-type.enum';


@Component({
    selector: 'app-itineraries',
    templateUrl: './itineraries.page.html',
    styleUrls: ['./itineraries.page.scss'],
})
export class ItinerariesPage {

    itineraries: Itinerary[];

    constructor(private userProvider: UserProvider,
                private driverService: DriverService,
                private itinerariesProvider: ItineraryProvider,
                private router: Router) {
    }

    ionViewWillEnter() {
        this.loadItineraries();
    }

    private loadItineraries() {
        this.driverService.getItinerariesByDriverID(this.userProvider.getDriverId()).subscribe(
            (response) => {
                const unsortedItineraries = response.body;
                this.itineraries = this.sortItineraries(unsortedItineraries);
                this.itinerariesProvider.setItineraries(this.itineraries);
            }
        );


    }

    showInfo() {
        this.router.navigate(['/app/settings/about', {from: 'itineraries'}]);
    }

    sortItineraries(unsortedItineraries: Itinerary[]) {
        const inProgressItinerary = unsortedItineraries.filter(itinerary => itinerary.itineraryStatusType === ItineraryStatusType.IN_PROGRESS);
        const readyToStartItineraries = unsortedItineraries
            .filter(itinerary => itinerary.itineraryStatusType === ItineraryStatusType.READY_TO_START)
            .sort((i1, i2) => {
                if (i1.scheduledTime > i2.scheduledTime) {
                    return 1;
                }
                if (i1.scheduledTime < i2.scheduledTime) {
                    return -1;
                }
                return 0;
            });
        const fishishedAndCanceledItineraries = unsortedItineraries
            .filter(itinerary => (itinerary.itineraryStatusType === ItineraryStatusType.FINISHED) || (itinerary.itineraryStatusType === ItineraryStatusType.CANCELED))
            .sort((i1, i2) => {
                if (i1.scheduledTime < i2.scheduledTime) {
                    return 1;
                }
                if (i1.scheduledTime > i2.scheduledTime) {
                    return -1;
                }
                return 0;
            });
        return inProgressItinerary.concat(readyToStartItineraries.concat(fishishedAndCanceledItineraries));
    }
}

