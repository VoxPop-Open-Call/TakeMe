import {Injectable} from '@angular/core';
import {ToastController} from '@ionic/angular';

@Injectable({
    providedIn: 'root'
})
export class ToastService {

    constructor(private toastController: ToastController) {
    }

    presentInfoToast(message: string, closeBtnText?: string, dismissFn?: Function) {
        this.presentToast(message, 'primary', closeBtnText, dismissFn);
    }

    presentErrorToast(message: string, closeBtnText?: string, dismissFn?: Function) {
        this.presentToast(message, 'danger', closeBtnText, dismissFn);
    }


    private async presentToast(message: string, color: string, closeBtnText?: string, dismissFn?: Function) {
        // this.closeToast();
        const toast = await this.toastController.create({
            message: message,
            position: 'top',
            color: color,
            showCloseButton: true,
            closeButtonText: closeBtnText ? closeBtnText : 'Fechar'
        });


        if (dismissFn) {
            toast.onDidDismiss().then(() => {
                dismissFn();
            });
        }

        toast.present();
    }

    async closeToast() {
        const activeToast = await this.toastController.getTop();
        if (activeToast) {
            activeToast.dismiss();
        }
    }



}
