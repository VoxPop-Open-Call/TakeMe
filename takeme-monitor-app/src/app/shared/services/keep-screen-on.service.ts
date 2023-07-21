import { Injectable } from '@angular/core';
import { Insomnia } from '@ionic-native/insomnia/ngx';
import { Logger } from '../providers/logger-provider.service';

@Injectable({
    providedIn: 'root'
})
export class KeepScreenOnService {

    constructor(private insomnia: Insomnia,
        private logger: Logger) { }

    keepAwake() {
        this.insomnia.keepAwake().then(
            () => this.logger.debug('Keeping screen awake...'),
            (error) => this.logger.error('Keep sleep awake success error.', error)
        );
    }

    allowSleepAgain() {
        this.insomnia.allowSleepAgain().then(
            () => this.logger.debug('Allowing screen to sleep again...'),
            (error) => this.logger.error('Allow sleep again error.', error)
        );
    }

}
