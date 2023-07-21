import {Injectable} from '@angular/core';
import {LoadingController} from '@ionic/angular';

@Injectable({
    providedIn: 'root'
})
export class LoadingControllerService {

    constructor(private loadingController: LoadingController) {
    }

    async createLoading(message: string, duration: number) {
        return await this.loadingController.create({
            message: message,
            duration: duration
        });
    }
}
