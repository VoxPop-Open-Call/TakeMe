import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Itinerary } from 'src/app/shared/models/itinerary';
import { ItineraryProvider } from '../providers/itinerary.provider.service';
import { ItineraryService } from 'src/app/shared/services/itinerary.service';
import { AlertController } from '@ionic/angular';
import { Vehicle } from '../../shared/models/vehicle';
import { OrganizationService } from '../../shared/services/organization.service';
import { UserProvider } from '../../shared/providers/user-provider.service';
import { ItineraryStatusType } from '../../shared/enums/itinerary-status-type.enum';
import { ItineraryStatusTypePatchDTO } from '../../shared/models/itinerary-status-type-patch-DTO';
import { Logger } from '../../shared/providers/logger-provider.service';
import { ToastService } from '../../shared/services/toast.service';

@Component({
    selector: 'app-itinerary-detail',
    templateUrl: './itinerary-detail.page.html',
    styleUrls: ['./itinerary-detail.page.scss'],
})
export class ItineraryDetailPage {

    LATE_TIME_TRESHOLD_IN_MILLISECONDS = 30 * 60 * 1000;
    itinerary: Itinerary;
    vehicles: Vehicle[];
    selectedVehicle: Vehicle;
    currentLatitude: number;
    currentLongitude: number;
    itineraryModified: boolean;

    constructor(private itineraryProvider: ItineraryProvider,
                private userProvider: UserProvider,
                private itineraryService: ItineraryService,
                private organizationService: OrganizationService,
                private toastService: ToastService,
                private router: Router,
                private alertController: AlertController,
                private logger: Logger
    ) {
    }

    ionViewWillEnter() {
        this.itineraryModified = false;
        this.preparePage();
    }

    preparePage() {
        this.vehicles = [];
        this.itineraryService.getItineraryDetails(this.itineraryProvider.getItineraryId()).subscribe((detailedItinerary) => {
            this.itinerary = detailedItinerary.body;
            this.organizationService.getVehiclesByOrganizationId(this.userProvider.getOrganizationId()).subscribe((listOfVehicles) => {
                this.vehicles = listOfVehicles.body;
                this.selectedVehicle = this.vehicles.filter(currentVehicle => currentVehicle.id === this.itinerary.vehicle.id)[0];
            });
            if (this.itinerary.itineraryStatusType === ItineraryStatusType.READY_TO_START) {
                this.logger.debug('Itinerary is ready to start, fetching current device location...');
                navigator.geolocation.getCurrentPosition((position) => {
                    this.currentLatitude = position.coords.latitude;
                    this.currentLongitude = position.coords.longitude;
                    this.logger.debug('Successfully retrieved device location coordinates: LAT ' + position.coords.latitude + ', LNG ' + position.coords.longitude);
                }, (error) => {
                    this.logger.debug('Could not retrieve device location.');
                    this.toastService.presentInfoToast('Serviços de localização têm de estar ativos.');
                });
            } else {
                this.logger.debug('Itinerary is not on a ready to start status. Will not fetch device location.');
            }
        });
    }

    goBack() {
        this.router.navigateByUrl('/app/itineraries');
    }

    onVehicleChange() {
        this.itineraryModified = true;
        this.itinerary.vehicle = this.selectedVehicle;
    }

    // reorderServices(detailOrder) {
    //     this.itineraryModified = true;
    //     const orderToSwap = this.itinerary.itineraryStopPointList[detailOrder.from].order;
    //     this.itinerary.itineraryStopPointList[detailOrder.from].order = this.itinerary.itineraryStopPointList[detailOrder.to].order;
    //     this.itinerary.itineraryStopPointList[detailOrder.to].order = orderToSwap;
    //     this.itinerary.itineraryStopPointList = detailOrder.complete(this.itinerary.itineraryStopPointList);
    // }

    private async startService() {
        const messagelate = ((new Date().getTime() - new Date(this.itinerary.scheduledTime.toString()).getTime()) > this.LATE_TIME_TRESHOLD_IN_MILLISECONDS) ? 'O percurso está a ser iniciado fora de horas.' : '';
        const message = 'O percurso não pode ser alterado após iniciar. <br><br> Tem a certeza que quer iniciar percurso com o veículo ' + '<span style="white-space:nowrap; font-weight: bold;">' + this.itinerary.vehicle.licensePlate + '</span>' + '?';

        const alert = await this.alertController.create({
            header: messagelate,
            message: message,
            buttons: [
                {
                    text: 'Não',
                    role: 'cancel',
                    cssClass: 'secondary',
                }, {
                    text: 'Sim',
                    handler: () => {
                        this.startItinerary(this.currentLatitude, this.currentLongitude);
                    }

                }
            ]
        });
        await alert.present();
    }

    private hasActiveItineraries() {
        return this.itineraryProvider.hasActiveItineraries();
    }

    private startItinerary(latitude: number, longitude: number) {
        const itineraryStatusTypePatchDTO = new ItineraryStatusTypePatchDTO();
        itineraryStatusTypePatchDTO.statusType = ItineraryStatusType.IN_PROGRESS;
        itineraryStatusTypePatchDTO.latitude = latitude;
        itineraryStatusTypePatchDTO.longitude = longitude;
        if (this.itineraryModified) {
            this.itineraryService.updateItinerary(this.itinerary).subscribe((itinerary) => {
                this.itinerary = itinerary.body;
                this.itineraryService.startItinerary(this.itineraryProvider.getItineraryId(), itineraryStatusTypePatchDTO).subscribe(() => {
                    this.router.navigateByUrl('/app/current-itinerary');
                });
            }, (error) => {
                this.logger.debug('It was not possible to change the itinerary. Reverting to previous state.');
                this.preparePage();
                if (error.error.errorKey && error.error.errorKey === 'vehicleCapacity') {
                    this.toastService.presentErrorToast('A capacidade do veículo selecionado não se ajusta ao percurso.');
                } else {
                    throw error;
                }

            });
        } else {
            this.itineraryService.startItinerary(this.itineraryProvider.getItineraryId(), itineraryStatusTypePatchDTO).subscribe(() => {
                this.router.navigateByUrl('/app/current-itinerary');
            });
        }
    }

}
