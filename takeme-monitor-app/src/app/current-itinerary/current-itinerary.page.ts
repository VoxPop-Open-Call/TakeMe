import { Component, NgZone, ViewChild } from '@angular/core';
import { Itinerary } from '../shared/models/itinerary';
import { DriverService } from '../shared/services/driver.service';
import { ItineraryStatusType } from '../shared/enums/itinerary-status-type.enum';
import { Router } from '@angular/router';
import { ItineraryService } from '../shared/services/itinerary.service';
import { UserProvider } from '../shared/providers/user-provider.service';
import { Logger } from '../shared/providers/logger-provider.service';
import { StopAuditEvent } from '../shared/models/stop-audit-event';
import { ChildStopEventDTO } from '../shared/models/child-stop-event-dto';
import { ToastService } from '../shared/services/toast.service';
import { KeepScreenOnService } from '../shared/services/keep-screen-on.service';
import { CurrentItineraryProvider } from '../shared/providers/current-itinerary-provider.service';


@Component({
    selector: 'app-current-itinerary',
    templateUrl: './current-itinerary.page.html',
    styleUrls: ['./current-itinerary.page.scss'],
})
export class CurrentItineraryPage {

    currentItinerary: Itinerary;
    inProgressIndex = 0;
    @ViewChild('slides', {static: false}) slides;

    slideOpts = {
        zoom: false
    };

    inProgressStopAuditEventList = new Array<StopAuditEvent>();

    constructor(private driverService: DriverService,
                private zone: NgZone,
                private router: Router,
                private userProvider: UserProvider,
                private itineraryService: ItineraryService,
                private logger: Logger,
                private toastService: ToastService,
                private keepScreenOnService: KeepScreenOnService,
                private ngZone: NgZone,
                private currentItineraryProvider: CurrentItineraryProvider
    ) {
    }

    ionViewWillEnter() {
        this.keepScreenOnService.keepAwake();
        this.refreshPage();
    }

    ionViewWillLeave() {
        this.keepScreenOnService.allowSleepAgain();
    }

    refreshPage() {
        this.ngZone.run(() => {
            this.inProgressIndex = 0;
            if (this.currentItineraryProvider.hasCurrentItinerary()) {
                this.currentItinerary = this.currentItineraryProvider.getCurrentItineraryAndResetProvider();
                this.preparePage();
            } else {
                this.driverService.getCurrentItinerary(this.userProvider.getDriverId()).subscribe((currentItineraryResponse) => {
                    if (currentItineraryResponse.status === 204) {
                        this.logger.debug('Received a code 204 response from backend, which means that there is no current' +
                            'itinerary for the monitor.');
                        this.currentItinerary = null;
                        this.logger.debug('Monitor has no current itinerary.');
                    } else {
                        this.currentItinerary = currentItineraryResponse.body;
                        this.preparePage();
                    }
                });
            }
        });
    }

    slideToInProgress() {
        if (this.slides) {
            this.slides.slideTo(this.inProgressIndex);
        } else {
            this.logger.debug('Element IonSlides is undefined, cannot slide to In Progress ItineraryStopPoint. Showing first in order.');
        }
    }

    finishStopPoint() {
        const stopManagementArray = new Array<ChildStopEventDTO>();
        this.inProgressStopAuditEventList.forEach(stopAuditEvent => {
            const childStopEventDTO = new ChildStopEventDTO();
            childStopEventDTO.childId = stopAuditEvent.child.id;
            childStopEventDTO.event = stopAuditEvent.eventType;
            stopManagementArray.push(childStopEventDTO);
        });

        this.itineraryService.finishStopPoint(this.currentItinerary.id, stopManagementArray).subscribe((itinerary) => {
            if (itinerary.body.itineraryStatusType === ItineraryStatusType.FINISHED) {
                this.itineraryFinished();
            } else {
                this.stopPointFinished();
            }
        });
    }

    stopPointFinished() {
        this.refreshPage();
    }

    itineraryFinished() {
        this.toastService.presentInfoToast('Percurso finalizado.');
        this.keepScreenOnService.allowSleepAgain();
        this.router.navigateByUrl('/app/itineraries');
    }

    preparePage() {
        const length = this.currentItinerary.itineraryStopPointList.length;
        this.currentItinerary.itineraryStopPointList[length - 1].last = true;
        for (const itineraryStopPoint of this.currentItinerary.itineraryStopPointList) {
            if (itineraryStopPoint.itineraryStatusType === ItineraryStatusType.IN_PROGRESS) {
                itineraryStopPoint.current = true;
                itineraryStopPoint.stopPoint.childList.forEach(child => {
                    const stopAuditEvent = new StopAuditEvent();
                    stopAuditEvent.child = child;
                    stopAuditEvent.eventType = null;
                    itineraryStopPoint.stopPoint.stopAuditEvents.push(stopAuditEvent);

                });
                this.inProgressStopAuditEventList = itineraryStopPoint.stopPoint.stopAuditEvents;
                break;
            }
            this.inProgressIndex++;
        }
        this.slideToInProgress();
    }

}
