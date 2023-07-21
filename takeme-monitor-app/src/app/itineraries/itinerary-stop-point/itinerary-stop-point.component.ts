import { Component, OnInit, Input } from '@angular/core';
import { ItineraryStopPoint } from 'src/app/shared/models/itinerary-stop-point';
import { ChildrenService } from 'src/app/shared/services/children.service';
import { AlertController } from '@ionic/angular';
import { Logger } from 'src/app/shared/providers/logger-provider.service';
import { Location } from '../../shared/models/location';
import { Contact } from '../../shared/models/contact';
import { LocationService } from '../../shared/services/location.service';


@Component({
    selector: 'app-itinerary-stop-point',
    templateUrl: './itinerary-stop-point.component.html',
    styleUrls: ['./itinerary-stop-point.component.scss']
})
export class ItineraryStopPointComponent implements OnInit {

    childPhotos: string[] = [];

    @Input()
    itineraryStopPoint: ItineraryStopPoint;

    constructor(private childrenService: ChildrenService,
        private locationService: LocationService,
        private alertController: AlertController,
        private logger: Logger) { }

    ngOnInit() {
        this.fetchPhotos();
    }

    private fetchPhotos() {
        this.itineraryStopPoint.stopPoint.childList.filter(child => child.photoId != null).forEach(
            (child, index) => {
                this.childrenService.getChildPhoto(child.id, child.photoId).subscribe(
                    (response) => {
                        this.childPhotos[index] = response.body.photo;
                    }
                );
            }
        );
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

    showChildDetail(child) {
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

}
