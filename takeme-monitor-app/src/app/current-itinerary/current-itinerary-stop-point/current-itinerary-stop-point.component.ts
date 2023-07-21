import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ItineraryStopPoint } from 'src/app/shared/models/itinerary-stop-point';
import { AlertController } from '@ionic/angular';
import { StopEventType } from 'src/app/shared/enums/stop-event-type.enum';
import { StopPointType } from '../../shared/enums/stop-point-type.enum';
import { ChildrenService } from '../../shared/services/children.service';
import { ItineraryStatusType } from '../../shared/enums/itinerary-status-type.enum';
import { Logger } from '../../shared/providers/logger-provider.service';
import { Location } from '../../shared/models/location';
import { Contact } from '../../shared/models/contact';
import { LocationService } from '../../shared/services/location.service';
import { Child } from '../../shared/models/child';


@Component({
    selector: 'app-current-itinerary-stop-point',
    templateUrl: './current-itinerary-stop-point.component.html',
    styleUrls: ['./current-itinerary-stop-point.component.scss']
})
export class CurrentItineraryStopPointComponent implements OnInit {

    @Input()
    itineraryStopPoint: ItineraryStopPoint;
    @Output()
    stopPointFinished = new EventEmitter<void>();

    differenceInMinutesInteger: number;
    timeString: string;

    constructor(private alertController: AlertController,
                private childrenService: ChildrenService,
                private locationService: LocationService,
                private logger: Logger
    ) {
    }

    ngOnInit() {
        this.fetchPhotos();
        this.getTimeDifference();
    }

    async finishStopPointDialog() {
        let message: string;
        if (this.itineraryStopPoint.last) {
            message = 'Tem a certeza que pretende concluir a última paragem do percurso?';
        } else {
            message = 'Tem a certeza que quer concluir esta paragem?';
        }
        const alert = await this.alertController.create({
            message: message,
            buttons: [
                {
                    text: 'Não',
                    role: 'cancel',
                    cssClass: 'secondary',
                }, {
                    text: 'Sim',
                    handler: () => {
                        this.finishStopPoint();
                    }
                }
            ]
        });
        await alert.present();
    }

    activateCancelButton(childId: string) {
        this.itineraryStopPoint.stopPoint.stopAuditEvents.find(x => x.child.id === childId).eventType = StopEventType.CANCELED;
    }

    deactivateCancelButton(childId: string) {
        this.itineraryStopPoint.stopPoint.stopAuditEvents.find(x => x.child.id === childId).eventType = null;
    }

    activateCheckInOrOutButton(childId: string) {
        let stopEventType: StopEventType;
        if (this.itineraryStopPoint.stopPoint.stopPointType === StopPointType.COLLECTION) {
            stopEventType = StopEventType.CHECK_IN;
        } else {
            stopEventType = StopEventType.CHECK_OUT;
        }
        this.itineraryStopPoint.stopPoint.stopAuditEvents.find(x => x.child.id === childId).eventType = stopEventType;
    }

    deactivateCheckInOrOutButton(childId: string) {
        this.itineraryStopPoint.stopPoint.stopAuditEvents.find(x => x.child.id === childId).eventType = null;
    }

    allChildStatusDefined(): boolean {
        const numberOfStopPoints = this.itineraryStopPoint.stopPoint.stopAuditEvents.length;
        let numberOfChildrenWithStatusDefined = 0;
        this.itineraryStopPoint.stopPoint.stopAuditEvents.forEach(itineraryStopPoint => {
            if (itineraryStopPoint.eventType !== null) {
                numberOfChildrenWithStatusDefined++;
            }
        });
        return numberOfStopPoints === numberOfChildrenWithStatusDefined;
    }

    finishStopPoint() {
        this.stopPointFinished.emit();
    }

    fetchPhotos() {
        if (this.itineraryStopPoint.itineraryStatusType === ItineraryStatusType.READY_TO_START) {
            this.itineraryStopPoint.stopPoint.childList.forEach(child => {
                if (child.photoId != null) {
                    this.childrenService.getChildPhoto(child.id, child.photoId).subscribe((photo) => {
                        child.photo = 'data:image/jpg;base64,' + photo.body.photo;
                    });
                }
            });
        }
        if (this.itineraryStopPoint.itineraryStatusType === ItineraryStatusType.FINISHED || this.itineraryStopPoint.itineraryStatusType === ItineraryStatusType.IN_PROGRESS) {
            this.itineraryStopPoint.stopPoint.stopAuditEvents.forEach(stopAuditEvent => {
                if (stopAuditEvent.child.photoId != null) {
                    this.childrenService.getChildPhoto(stopAuditEvent.child.id, stopAuditEvent.child.photoId).subscribe((photo) => {
                        stopAuditEvent.child.photo = 'data:image/jpg;base64,' + photo.body.photo;
                    });
                }
            });
        }
    }

    showDestinationLocationDetail(location: Location) {
        if (location.type === 'SCHOOL') {
            this.locationService.getOrganizationPublicContactsByLocationId(location.id).subscribe((contacts) => {
                const listOfContacts = contacts.body;
                this.showDestinationLocationDetailAlert(location.designation, listOfContacts);
            });
        } else if (location.type === 'SCHOOL_EXTERNAL') {
            this.showDestinationLocationDetailAlert(location.designation, []);
        }
    }

    async showDestinationLocationDetailAlert(locationDesignation, listOfContacts: Contact[]) {
        let message = '<span class="pop-up-title">' + locationDesignation + '</span>';
        if (listOfContacts.length > 0) {
            listOfContacts.forEach((contact) => {
                const contactInfo = '<br><br>' + contact.name
                    + '<br><ion-button fill="clear" size="small" color="dark" href="tel:' + contact.phoneNumber
                    + '"><ion-icon name="call"></ion-icon>&nbsp' + contact.phoneNumber
                    + '</ion-button>';
                message = message.concat(contactInfo);
            });
        } else {
            const noContactInfo = '<br><br><ion-icon name="call"></ion-icon>&nbsp' + 'Sem contato';
            message = message.concat(noContactInfo);
        }
        const alert = await this.alertController.create({
            message: message,
            buttons: [
                {
                    text: 'ok',
                    role: 'cancel',
                    handler: () => {
                        this.logger.debug('[CurrentItineraryStopPointComponent : organizationDetailAlert], ok clicked');
                    }
                }]
        });
        await alert.present();
    }

    showChildDetail(child: Child) {
        const childName = child.name;
        this.childrenService.getTutorsByChildId(child.id).subscribe((tutors) => {
            const listOfTutors = tutors.body;
            this.childDetailAlert(childName, listOfTutors);
        });
    }

    async childDetailAlert(childName, listOfTutors) {
        let message = '<span class="pop-up-title">' + childName + '</span>';
        listOfTutors.forEach((tutor) => {
            const tutorInfo = '<br><br>' + tutor.name
                + '<br><ion-button fill="clear" size="small" color="dark" href="tel:' + tutor.phoneNumber
                + '"><ion-icon name="call"></ion-icon>&nbsp' + tutor.phoneNumber
                + '</ion-button>';
            message = message.concat(tutorInfo);
        });
        const alert = await this.alertController.create({
            message: message,
            buttons: [
                {
                    text: 'ok',
                    role: 'cancel',
                    handler: () => {
                        this.logger.debug('[CurrentItineraryStopPointComponent : childDetailAlert], ok clicked');
                    }
                }]
        });
        await alert.present();
    }

    getTimeDifference() {
        let effectiveOrEstimatedDateDateString;
        if (this.itineraryStopPoint.itineraryStatusType === ItineraryStatusType.FINISHED) {
            effectiveOrEstimatedDateDateString = this.itineraryStopPoint.stopPoint.effectiveArriveTime;
        } else if (this.itineraryStopPoint.itineraryStatusType === ItineraryStatusType.IN_PROGRESS || this.itineraryStopPoint.itineraryStatusType === ItineraryStatusType.READY_TO_START) {
            effectiveOrEstimatedDateDateString = this.itineraryStopPoint.stopPoint.estimatedArriveTime;
            if (effectiveOrEstimatedDateDateString === null) {
                return null;
            }
        }
        const scheduledDate = new Date(this.itineraryStopPoint.stopPoint.scheduledTime);
        const effectiveOrEstimatedDate = new Date(effectiveOrEstimatedDateDateString);
        const differenceInMilliseconds = effectiveOrEstimatedDate.getTime() - scheduledDate.getTime();
        const differenceInSeconds = differenceInMilliseconds / 1000;
        const differenceInMinutes = differenceInSeconds / 60;
        const differenceInMinutesInteger = Math.trunc(differenceInMinutes);
        this.differenceInMinutesInteger = differenceInMinutesInteger;
        const absoluteDifferenceInMinutesInteger = Math.abs(differenceInMinutesInteger);
        if (differenceInMinutesInteger > 0) {
            this.timeString = 'Atrasado ' + absoluteDifferenceInMinutesInteger + '\u00A0' + 'min';
        } else if (differenceInMinutesInteger < 0) {
            this.timeString = 'Adiantado ' + absoluteDifferenceInMinutesInteger + '\u00A0' + 'min';
        } else {
            this.timeString = 'A horas';
        }
    }

    async showTimeDetails() {
        let message = '';
        const scheduledTimeRow = '<span><ion-icon name="time"></ion-icon><b> Hora combinada:</b> ' + this.formatTime(this.itineraryStopPoint.stopPoint.scheduledTime) + '</span>';
        message = message.concat(scheduledTimeRow);
        if (this.itineraryStopPoint.itineraryStatusType === ItineraryStatusType.IN_PROGRESS || this.itineraryStopPoint.itineraryStatusType === ItineraryStatusType.READY_TO_START) {
            const estimatedArriveTimeValue = this.itineraryStopPoint.stopPoint.estimatedArriveTime;
            if (estimatedArriveTimeValue !== null) {
                const estimatedArriveTimeRow = '<br><span><ion-icon name="time"></ion-icon><b> Hora estimada:</b> ' + this.formatTime(this.itineraryStopPoint.stopPoint.estimatedArriveTime) + '</span>';
                message = message.concat(estimatedArriveTimeRow);
            }
        }
        const effectiveArriveTimeValue = this.itineraryStopPoint.stopPoint.effectiveArriveTime;
        if (effectiveArriveTimeValue !== null) {
            const effectiveArriveTimeRow = '<br><span><ion-icon name="time"></ion-icon><b> Hora efetiva:</b> ' + this.formatTime(this.itineraryStopPoint.stopPoint.effectiveArriveTime) + '</span>';
            message = message.concat(effectiveArriveTimeRow);
        }
        const alert = await this.alertController.create({
            message: message,
            buttons: [
                {
                    text: 'ok',
                    role: 'cancel',
                    handler: () => {
                        this.logger.debug('[CurrentItineraryStopPointComponent : childTimeDetailAlert], ok clicked');
                    }
                }]
        });
        await alert.present();
    }

    formatTime(unformattedDateTimeString: string): string {
        const date = new Date(unformattedDateTimeString);
        const hoursNumber = date.getHours();
        const minutesNumber = date.getMinutes();
        let minutesString = '';
        if (minutesNumber < 10) {
            minutesString = minutesString.concat('0');
        }
        minutesString = minutesString.concat(minutesNumber.toString());
        const secondsNumber = date.getSeconds();
        let secondsString = '';
        if (secondsNumber < 10) {
            secondsString = secondsString.concat('0');
        }
        secondsString = secondsString.concat(secondsNumber.toString());
        return hoursNumber.toString() + ':' + minutesString + ':' + secondsString;
    }

}
