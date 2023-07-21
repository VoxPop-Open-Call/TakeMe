import { FirebaseConfigModel } from 'app/core/models/firebase-config.model';
import { Environment } from '../../../environments/environment';
import { AngularFireModule, FIREBASE_OPTIONS } from '@angular/fire/compat';

export class Angularfire2ConfigModule {
    static initializeApp(configFactory: (env: Environment) => FirebaseConfigModel) {
        return {
            ngModule: AngularFireModule,
            providers: [{ provide: FIREBASE_OPTIONS, useFactory: configFactory, deps: [Environment] }]
        };
    }
}
